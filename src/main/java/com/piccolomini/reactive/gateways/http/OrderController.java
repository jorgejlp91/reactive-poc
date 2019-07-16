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

    // It won't throws an exception if the order doesn't exists
    Mono<Order> orderMono2 =
        gateway.findOne(id2).doOnNext(o -> log.debug("Get order 2 by id {}", id2));

    // if the orderMono isn't present in return statement,
    // when occurs an error, never will throw because there is no subscriber listening
    return Flux.concat(orderMono, orderMono2);
  }

  @GetMapping("/handledException/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Order> findHandledException(@PathVariable final Long id) {
    return gateway.findOne(id).switchIfEmpty(Mono.error(new OrderNotFoundException(id)));
  }

  @GetMapping("/unhandledException/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Order> findUnHandledException(@PathVariable final Long id) {
    return gateway.findOne(id).doOnNext(this::throwException);
  }

  private Mono<Order> throwException(Order order) {
    throw new NullPointerException("test");
  }

  @GetMapping("/infiniteOrder/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Flux<Order> findInfinite(@PathVariable final Long id) {
    return gateway
        .findEmpty()
        .switchIfEmpty(
            subscriber -> {
              log.warn("this will never execute");
              // without a return statement , the stream never proceed, causing an infinite wait
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
              // because there is no subscriber listening
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
