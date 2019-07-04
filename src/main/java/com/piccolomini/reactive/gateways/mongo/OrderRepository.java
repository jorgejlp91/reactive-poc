package com.piccolomini.reactive.gateways.mongo;

import com.piccolomini.reactive.domains.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {}
// {
//  <S extends Order> Mono<S> save(S var1);
//
//  <S extends Order> Flux<S> saveAll(Iterable<S> var1);
//
//  <S extends Order> Flux<S> saveAll(Publisher<S> var1);
//
//  Mono<Order> findById(Long var1);
//
//  Mono<Order> findById(Publisher<Long> var1);
//
//  Mono<Boolean> existsById(Long var1);
//
//  Mono<Boolean> existsById(Publisher<Long> var1);
//
//  Flux<Order> findAll();
//
//  Flux<Order> findAllById(Iterable<Long> var1);
//
//  Flux<Order> findAllById(Publisher<Long> var1);
//
//  Mono<Long> count();
//
//  Mono<Void> deleteById(Long var1);
//
//  Mono<Void> deleteById(Publisher<Long> var1);
//
//  Mono<Void> delete(Order var1);
//
//  Mono<Void> deleteAll(Iterable<? extends Order> var1);
//
//  Mono<Void> deleteAll(Publisher<? extends Order> var1);
//
//  Mono<Void> deleteAll();
// }
