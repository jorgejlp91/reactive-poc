package com.piccolomini.reactive.gateways;

import com.piccolomini.reactive.domains.Order;
import reactor.core.publisher.Mono;

public interface OrderStatusGateway {

  Mono<Order> getStatus(Long orderId);
}
