package com.piccolomini.reactive.gateways;

import com.piccolomini.reactive.domains.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderGateway {

  Mono<Order> save(Order order);

  Mono<Order> findOne(Long id);

  Mono<Order> throwException();

  Mono<Order> throwMonoError();

  Mono<Void> deleteOne(Long id);

  Flux<Order> findAll();

  Flux<Order> findEmpty();
}
