package com.piccolomini.reactive.gateways.postgres;

import com.piccolomini.reactive.domains.Account;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

@ConditionalOnExpression(value = "false")
public interface AccountRepository extends R2dbcRepository<Account, Long> {

  //  // java.lang.UnsupportedOperationException: Query derivation not yet supported!
  //  Mono<Account> findByUsername(String username);
  //
  //  // java.lang.UnsupportedOperationException: Query derivation not yet supported!
  //  Mono<Void> deleteByUsername(String username);
}
