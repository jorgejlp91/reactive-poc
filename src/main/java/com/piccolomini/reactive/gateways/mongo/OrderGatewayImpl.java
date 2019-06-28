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

  @Autowired
  public OrderGatewayImpl(final OrderRepository repository) {
    this.repository = repository;
  }

  @Override
  public Mono<Order> save(final Order product) {
    return repository.save(product);
  }

  @Override
  public Mono<Order> findOne(final Long id) {
    return repository.findById(id);
  }

  public Mono<Void> deleteOne(final Long id) {
    return repository.deleteById(id);
  }

  @Override
  public Flux<Order> findAll() {
    return repository.findAll();
  }
}
