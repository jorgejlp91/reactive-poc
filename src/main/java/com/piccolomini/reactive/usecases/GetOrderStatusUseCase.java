package com.piccolomini.reactive.usecases;

import com.piccolomini.reactive.domains.Order;
import com.piccolomini.reactive.gateways.OrderGateway;
import com.piccolomini.reactive.gateways.OrderStatusGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Component
public class GetOrderStatusUseCase {

  private final OrderGateway orderGateway;
  private final OrderStatusGateway orderStatusGateway;

  @Autowired
  public GetOrderStatusUseCase(OrderGateway orderGateway, OrderStatusGateway orderStatusGateway) {
    this.orderGateway = orderGateway;
    this.orderStatusGateway = orderStatusGateway;
  }

  public Flux<Order> execute(ServerRequest request) {

    log.info("getting the order status");

    return orderGateway
        .findAll()
        .flatMap(
            order -> {
              log.info("calling status service for order {}", order.getId());
              return Flux.combineLatest(
                  Mono.just(order),
                  orderStatusGateway.getStatus(order.getId()),
                  (o, orderStatus) -> new Order(o, orderStatus.getStatus()));
            })
        .filter(order -> BigDecimal.valueOf(10.00).compareTo(order.getUnitPrice()) < 0);
  }
}
