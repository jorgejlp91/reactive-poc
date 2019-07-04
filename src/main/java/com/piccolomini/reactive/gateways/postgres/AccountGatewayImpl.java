package com.piccolomini.reactive.gateways.postgres;

import com.piccolomini.reactive.domains.Account;
import com.piccolomini.reactive.domains.exceptions.AccountNotFoundException;
import com.piccolomini.reactive.gateways.AccountGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.r2dbc.function.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AccountGatewayImpl implements AccountGateway {

  private final AccountRepository accountRepository;
  private final DatabaseClient databaseClient;

  @Autowired
  public AccountGatewayImpl(
      @Qualifier("rxAccountRepository") AccountRepository accountRepository,
      DatabaseClient databaseClient) {
    this.accountRepository = accountRepository;
    this.databaseClient = databaseClient;
  }

  @Override
  public Mono<Account> save(Account account) {
    return accountRepository.save(account);
  }

  @Override
  public Mono<Account> findOne(String username) {
    return databaseClient
        .select()
        .from(String.format("account WHERE username = '%s'", username))
        //  We don't have a bind method here
        .fetch()
        .one()
        .flatMap(
            objMap -> {
              Account account = new Account();
              account.setUser_id((Integer) objMap.get("user_id"));
              account.setUsername(String.valueOf(objMap.get("username")));
              account.setPassword(String.valueOf(objMap.get("password")));
              account.setEmail(String.valueOf(objMap.get("email")));
              return Mono.just(account);
            });
  }

  @Override
  public Mono<Void> deleteOne(String username) {
    return databaseClient
        .execute()
        .sql(String.format("DELETE FROM account WHERE username = '%s'", username))
        // Binding parameters is not supported for the statement 'DELETE'
        // .bind(":username", username)
        .fetch()
        .rowsUpdated()
        .flatMap(
            rowsUpdated -> {
              if (rowsUpdated > 1 || rowsUpdated == 0) {
                return Mono.error(new AccountNotFoundException(username));
              } else {
                return Mono.empty();
              }
            });
  }

  @Override
  public Flux<Account> findAll() {
    return accountRepository.findAll();
  }

  @Override
  public Mono<Account> update(Account account) {
    // Its not work for update, the save method just creates an INSERT INTO Statement
    // return accountRepository.save(account);
    return databaseClient
        .execute()
        .sql(
            String.format(
                "UPDATE account set password = '%s', email = '%s' WHERE username = '%s'",
                account.getPassword(), account.getEmail(), account.getUsername()))
        // Binding parameters is not supported for the statement 'UPDATE'
        //        .bind(":password", account.getPassword())
        //        .bind(":email", account.getEmail())
        //        .bind(":username", account.getUsername())
        .fetch()
        .rowsUpdated()
        .flatMap(
            rowsUpdated -> {
              if (rowsUpdated > 1 || rowsUpdated == 0) {
                return Mono.error(new AccountNotFoundException(account.getUsername()));
              } else {
                return Mono.just(account);
              }
            });
  }
}
