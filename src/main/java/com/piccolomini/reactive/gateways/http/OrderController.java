package com.piccolomini.reactive.gateways.http;

import com.piccolomini.reactive.domains.Order;
import com.piccolomini.reactive.domains.exceptions.OrderNotFoundException;
import com.piccolomini.reactive.gateways.OrderGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
public class OrderController {

  private OrderGateway gateway;

  @Autowired
  public OrderController(final OrderGateway gateway) {
    this.gateway = gateway;
  }

  @PostMapping("/order")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Order> create(@Valid @RequestBody final Order product) {
    return gateway.save(product).doOnNext(p -> log.debug("Create new order - {}", p));
  }

  @PutMapping("/order")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Order> update(@Valid @RequestBody final Mono<Order> product) {
    return product.flatMap(
        p -> gateway.save(p).doOnNext(prod -> log.info("Updating oder - {}", prod)));
  }

  @GetMapping("/order/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Order> find(@PathVariable final Long id) {
    return gateway
        .findOne(id)
        .switchIfEmpty(Mono.error(new OrderNotFoundException(id)))
        .doOnNext(p -> log.debug("Get order by id {}", id));
  }

  @DeleteMapping("/order/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<Void> delete(@PathVariable final Long id) {
    return gateway.deleteOne(id);
  }
}
