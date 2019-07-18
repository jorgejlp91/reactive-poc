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

  @PostMapping("/orders")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Order> create(@Valid @RequestBody final Order order) {
    return gateway.save(order).doOnNext(o -> log.debug("Create new order - {}", o));
  }

  @PutMapping("/orders")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Order> update(@Valid @RequestBody final Mono<Order> order) {
    return order.flatMap(o -> gateway.save(o).doOnNext(or -> log.info("Updating order - {}", or)));
  }

  @GetMapping("/orders/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Order> find(@PathVariable final Long id) {
    return gateway
        .findOne(id)
        .switchIfEmpty(Mono.error(new OrderNotFoundException(id)))
        .doOnNext(o -> log.debug("Get order by id {}", id));
  }

  @DeleteMapping("/orders/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<Void> delete(@PathVariable final Long id) {
    return gateway.deleteOne(id);
  }
}
