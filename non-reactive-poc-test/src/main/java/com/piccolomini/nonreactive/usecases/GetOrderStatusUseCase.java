package com.piccolomini.nonreactive.usecases;

import com.google.common.collect.ImmutableMap;
import com.piccolomini.nonreactive.domains.Order;
import com.piccolomini.nonreactive.gateways.OrderGateway;
import com.piccolomini.nonreactive.gateways.OrderStatusGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GetOrderStatusUseCase {

  private final OrderGateway orderGateway;
  private final OrderStatusGateway orderStatusGateway;

  private final Map<Long, String> statusMap =
      ImmutableMap.of(0L, "UNDEFINED", 1L, "PROCESSING", 2L, "PAID", 3L, "CANCELED");

  @Autowired
  public GetOrderStatusUseCase(OrderGateway orderGateway, OrderStatusGateway orderStatusGateway) {
    this.orderGateway = orderGateway;
    this.orderStatusGateway = orderStatusGateway;
  }

  public List<Order> execute() {

    log.info("getting the order status");
    List<Order> orderList = orderGateway.findAll();
    orderList
        .parallelStream()
        .forEach(
            order -> {
              log.info("calling status service for order {}", order.getId());
              order.setStatus(getStatus(orderStatusGateway.getStatus(order.getId())));
            });
    return orderList
        .parallelStream()
        .filter(order -> BigDecimal.valueOf(10.00).compareTo(order.getUnitPrice()) < 0)
        .collect(Collectors.toList());
  }

  private String getStatus(Long status) {
    return statusMap.get(status);
  }
}
