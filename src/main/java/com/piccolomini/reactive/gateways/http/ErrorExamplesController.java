package com.piccolomini.reactive.gateways.http;

import com.piccolomini.reactive.domains.Order;
import com.piccolomini.reactive.domains.exceptions.OrderNotFoundException;
import com.piccolomini.reactive.gateways.OrderGateway;
import com.piccolomini.reactive.gateways.OrderStatusGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RestController
public class ErrorExamplesController {

  private OrderGateway gateway;
  private OrderStatusGateway statusGateway;

  @Autowired
  public ErrorExamplesController(final OrderGateway gateway, OrderStatusGateway statusGateway) {
    this.gateway = gateway;
    this.statusGateway = statusGateway;
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
  public Flux<Order> findHandledException(@PathVariable final Long id) {
    return gateway.findEmpty().switchIfEmpty(Mono.error(new OrderNotFoundException(id)));
  }

  @GetMapping("/unhandledException")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Order> findUnHandledException() {
    return gateway.findOne(1L).doOnNext(this::throwException);
  }

  @GetMapping("/notFoundButOk")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Order> findWithoutRegister() {
    // This will response 200 OK but nothing will return in response body
    return gateway
        .findOne(555L)
        .doOnNext(
            order -> log.info("This will never execute because there no is order 555 in database"));
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

  @GetMapping("/throw")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Order> simpleThrowExceptionNonReactiveWay() {
    throw new RuntimeException("exception that will handled by ExceptionHandler mechanism");
  }

  @GetMapping("/handling")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Order> handleError() {
    return gateway
        .throwException()
        // This will not execute because the gataway throws an non reactive exception
        // throw new RuntimeException...
        .onErrorResume(e -> Mono.error(new OrderNotFoundException(999L)));
  }

  @GetMapping("/handlingReactive")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Order> handleReactiveError() {
    return gateway
        .throwMonoError()
        // This will execute because throws an Mono.error
        // Mono.error(new Exception....)
        .onErrorResume(e -> Mono.error(new OrderNotFoundException(999L)));
  }
}
