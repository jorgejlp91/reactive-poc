package com.piccolomini.reactive.gateways.http;

import com.piccolomini.reactive.domains.Account;
import com.piccolomini.reactive.domains.exceptions.AccountNotFoundException;
import com.piccolomini.reactive.gateways.AccountGateway;
import com.piccolomini.reactive.usecases.UpdateAccountUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
public class AccountController {

  private final AccountGateway gateway;
  private final UpdateAccountUseCase updateAccountUseCase;

  @Autowired
  public AccountController(
      AccountGateway accountGateway, UpdateAccountUseCase updateAccountUseCase) {
    this.gateway = accountGateway;
    this.updateAccountUseCase = updateAccountUseCase;
  }

  @PostMapping("/accounts")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Account> create(@Valid @RequestBody final Account account) {
    return gateway.save(account).doOnNext(acc -> log.debug("Create new account - {}", acc));
  }

  @PutMapping("/accounts")
  public Mono<ResponseEntity<Account>> update(@Valid @RequestBody final Mono<Account> account) {
    return updateAccountUseCase
        .execute(account)
        // map package anything inside its method into a Mono
        // flatMap just return what contains into the method
        .map(
            acc -> {
              if (acc.getUser_id() == null) {
                return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(acc);
              } else {
                return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(acc);
              }
            });
  }

  @GetMapping("/accounts/{username}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Account> find(@PathVariable final String username) {
    return gateway
        .findOne(username)
        .switchIfEmpty(Mono.error(new AccountNotFoundException(username)))
        .doOnNext(acc -> log.debug("Get account by username {}", username));
  }

  @GetMapping("/accounts")
  @ResponseStatus(HttpStatus.OK)
  public Flux<Account> findAll() {
    return gateway.findAll();
  }

  @DeleteMapping("/accounts/{username}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<Void> delete(@PathVariable final String username) {
    return gateway.deleteOne(username);
  }
}
