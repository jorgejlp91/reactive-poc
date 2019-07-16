package com.piccolomini.reactive.gateways.mongo;

import com.piccolomini.reactive.domains.Order;
import com.piccolomini.reactive.gateways.OrderGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class OrderGatewayImpl implements OrderGateway {

  private OrderRepository repository;
  private OrderRepository nullRepository;

  @Autowired
  public OrderGatewayImpl(final OrderRepository repository) {
    this.repository = repository;
  }

  @Override
  public Mono<Order> save(final Order order) {
    return repository.save(order);
  }

  @Override
  public Mono<Order> findOne(final Long id) {

    //    return Mono.from(nullRepository.findById(id))
    //        .onErrorResume(
    //            throwable -> {
    //              System.out.println("OCORREU UM ERRO INESPERADO");
    //              return Mono.error(new OrderNotFoundException(id));
    //            });
    // throw new OrderNotFoundException(999L);

    return repository.findById(id);

    //    return Mono.from(repository.findById(id)).delaySubscription(Duration.ofMillis(500L));
  }

  public Mono<Void> deleteOne(final Long id) {
    return repository.deleteById(id);
  }

  @Override
  public Flux<Order> findAll() {
    return repository.findAll();
  }

  @Override
  public Flux<Order> findEmpty() {
    return Flux.empty();
  }
}
