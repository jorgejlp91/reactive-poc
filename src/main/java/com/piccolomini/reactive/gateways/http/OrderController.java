package com.piccolomini.reactive.gateways.http;

import com.piccolomini.reactive.domains.Order;
import com.piccolomini.reactive.domains.exceptions.OrderNotFoundException;
import com.piccolomini.reactive.gateways.OrderGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
public class OrderController {

  private OrderGateway gateway;
  private OrderGateway nullGateway;

  @Autowired
  public OrderController(final OrderGateway gateway) {
    this.gateway = gateway;
  }

  @PostMapping("/orders")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Order> create(@Valid @RequestBody final Order product) {
    return gateway.save(product).doOnNext(p -> log.debug("Create new order - {}", p));
  }

  @PutMapping("/orders")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Order> update(@Valid @RequestBody final Mono<Order> product) {
    return product.flatMap(
        p -> gateway.save(p).doOnNext(prod -> log.info("Updating order - {}", prod)));
  }

  @GetMapping("/orders/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Order> find(@PathVariable final Long id) {
    return gateway
        .findOne(id)
        .switchIfEmpty(Mono.error(new OrderNotFoundException(id)))
        .doOnNext(p -> log.debug("Get order by id {}", id));
  }

  @DeleteMapping("/orders/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<Void> delete(@PathVariable final Long id) {
    return gateway.deleteOne(id);
  }

  // FLUXOS DE ERRO
  @GetMapping("/combineOrders/{id}/{id2}")
  @ResponseStatus(HttpStatus.OK)
  public Flux<Order> findTwo(@PathVariable final Long id, @PathVariable final Long id2) {
    Mono<Order> orderMono =
        gateway
            .findOne(id)
            .switchIfEmpty(Mono.error(new OrderNotFoundException(id)))
            .doOnNext(p -> log.debug("Get order by id {}", id));

    // Não vai lançar exceção se não achar
    Mono<Order> orderMono2 =
        gateway.findOne(id2).doOnNext(p -> log.debug("Get order 2 by id {}", id2));

    return Flux.concat(orderMono, orderMono2);
  }

  @GetMapping("/nullOrder/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Order> findNull(@PathVariable final Long id) {
    return gateway
        .findOne(id)
        .switchIfEmpty(Mono.error(new OrderNotFoundException(id)))
        .doOnNext(p -> nullGateway.findOne(id))
        .onErrorReturn(new Order(999L));
  }
}
