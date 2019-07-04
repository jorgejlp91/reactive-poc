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
import java.util.concurrent.atomic.AtomicBoolean;

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

  // FLUXOS DE ERRO
  @GetMapping("/combineOrders/{id}/{id2}")
  @ResponseStatus(HttpStatus.OK)
  public Flux<Order> findTwo(@PathVariable final Long id, @PathVariable final Long id2) {
    Mono<Order> orderMono =
        gateway
            .findOne(id)
            .switchIfEmpty(Mono.error(new OrderNotFoundException(id)))
            .doOnNext(o -> log.debug("Get order by id {}", id));

    // Não vai lançar exceção se não achar
    Mono<Order> orderMono2 =
        gateway.findOne(id2).doOnNext(o -> log.debug("Get order 2 by id {}", id2));

    return Flux.concat(orderMono, orderMono2);
  }

  @GetMapping("/exception/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Order> findNull(@PathVariable final Long id) {
    return gateway
        .findOne(id)
        .switchIfEmpty(Mono.error(new OrderNotFoundException(id)))
        .doOnNext(p -> throwException(id))
        .onErrorReturn(new Order(999L));
  }

  private Mono<Order> throwException(Long id) {
    throw new NullPointerException("test");
  }

  @GetMapping("/infiniteOrder/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Flux<Order> findInfinite(@PathVariable final Long id) {
    return gateway
        .findAll()
        .switchIfEmpty(
            subscriber -> {
              log.warn("this will never execute");
              Mono.error(new OrderNotFoundException(id));
            })
        .doOnNext(p -> log.info("Get all orders infinite by id {}", id));
  }

  @GetMapping("/orderNoExec/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Order> findNoExecute(@PathVariable final Long id) {

    final AtomicBoolean test = new AtomicBoolean(false);

    gateway
        .findOne(id)
        .doOnNext(
            find -> {
              log.error("this will never execute even order was found ");
              test.set(true);
            });

    log.info("result of first find one: {}", test);

    return Mono.just(test)
        .flatMap(
            t -> {
              if (t.get()) {
                return Mono.just(new Order(444L));
              } else {
                return Mono.just(new Order(555L));
              }
            });
  }
}
