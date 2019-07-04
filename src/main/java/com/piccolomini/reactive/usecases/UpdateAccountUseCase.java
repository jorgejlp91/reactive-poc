package com.piccolomini.reactive.usecases;

import com.piccolomini.reactive.domains.Account;
import com.piccolomini.reactive.gateways.AccountGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class UpdateAccountUseCase {

  private final AccountGateway accountGateway;

  @Autowired
  public UpdateAccountUseCase(AccountGateway accountGateway) {
    this.accountGateway = accountGateway;
  }

  public Mono<Account> execute(Mono<Account> accountMono) {
    return accountMono.flatMap(
        account ->
            accountGateway
                .findOne(account.getUsername())
                .flatMap(
                    accountFound -> {
                      log.info("account found, updating");
                      return accountGateway.update(account);
                    })
                .switchIfEmpty(logAndSave(account)));
  }

  private Mono<Account> logAndSave(Account account) {
    log.info("account not found, inserting");
    return accountGateway.save(account);
  }
}
