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
  public Mono<Order> save(final Order product) {
    return repository.save(product);
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
  }

  public Mono<Void> deleteOne(final Long id) {
    return repository.deleteById(id);
  }

  @Override
  public Flux<Order> findAll() {
    return repository.findAll();
  }
}
