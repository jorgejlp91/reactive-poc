package com.piccolomini.reactive.gateways.http.handlers;

import com.google.common.collect.ImmutableMap;
import com.piccolomini.reactive.domains.Order;
import com.piccolomini.reactive.gateways.OrderGateway;
import com.piccolomini.reactive.gateways.OrderStatusGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Component
public class OrderStatusHandler {

  private final OrderGateway orderGateway;
  private final OrderStatusGateway orderStatusGateway;

  private final Map<Long, String> statusMap =
      ImmutableMap.of(0L, "UNDEFINED", 1L, "PROCESSING", 2L, "PAID", 3L, "CANCELED");

  @Autowired
  public OrderStatusHandler(OrderGateway orderGateway, OrderStatusGateway orderStatusGateway) {
    this.orderGateway = orderGateway;
    this.orderStatusGateway = orderStatusGateway;
  }

  public Mono<ServerResponse> listOrderStatus(ServerRequest request) {

    log.info("getting the order status");

    Flux<Order> orderStatusList =
        orderGateway
            .findAll()
            .flatMap(
                order -> {
                  log.info("calling status service for order {}", order.getId());
                  return Flux.combineLatest(
                      Mono.just(order),
                      orderStatusGateway.getStatus(order.getId()),
                      (o, status) -> new Order(o, getStatus(status)));
                })
            .filter(order -> BigDecimal.valueOf(10.00).compareTo(order.getUnitPrice()) < 0);

    return ok().contentType(MediaType.APPLICATION_JSON)
        .body(orderStatusList, Order.class)
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  private String getStatus(Long status) {
    return statusMap.get(status);
  }
}
