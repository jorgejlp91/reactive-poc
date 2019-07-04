package com.piccolomini.reactive.gateways;

import com.piccolomini.reactive.domains.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountGateway {

  Mono<Account> save(Account account);

  Mono<Account> findOne(String username);

  Mono<Void> deleteOne(String username);

  Flux<Account> findAll();

  Mono<Account> update(Account account);
}
