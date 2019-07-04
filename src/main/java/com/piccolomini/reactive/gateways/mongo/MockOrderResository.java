package com.piccolomini.reactive.gateways.mongo;

import com.piccolomini.reactive.domains.Order;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MockOrderResository implements OrderRepository {
  @Override
  public <S extends Order> Mono<S> save(S var1) {
    return null;
  }

  @Override
  public <S extends Order> Flux<S> saveAll(Iterable<S> var1) {
    return null;
  }

  @Override
  public <S extends Order> Flux<S> saveAll(Publisher<S> var1) {
    return null;
  }

  @Override
  public Mono<Order> findById(Long var1) {
    return null;
  }

  @Override
  public Mono<Order> findById(Publisher<Long> var1) {
    return null;
  }

  @Override
  public Mono<Boolean> existsById(Long var1) {
    return null;
  }

  @Override
  public Mono<Boolean> existsById(Publisher<Long> var1) {
    return null;
  }

  @Override
  public Flux<Order> findAll() {
    return null;
  }

  @Override
  public Flux<Order> findAllById(Iterable<Long> var1) {
    return null;
  }

  @Override
  public Flux<Order> findAllById(Publisher<Long> var1) {
    return null;
  }

  @Override
  public Mono<Long> count() {
    return null;
  }

  @Override
  public Mono<Void> deleteById(Long var1) {
    return null;
  }

  @Override
  public Mono<Void> deleteById(Publisher<Long> var1) {
    return null;
  }

  @Override
  public Mono<Void> delete(Order var1) {
    return null;
  }

  @Override
  public Mono<Void> deleteAll(Iterable<? extends Order> var1) {
    return null;
  }

  @Override
  public Mono<Void> deleteAll(Publisher<? extends Order> var1) {
    return null;
  }

  @Override
  public Mono<Void> deleteAll() {
    return null;
  }
}
