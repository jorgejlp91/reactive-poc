package com.piccolomini.reactive.gateways;

import reactor.core.publisher.Mono;

public interface OrderStatusGateway {

  Mono<Long> getStatus(Long orderId);

  default String sanitize(final String content) {
    return content.replace("\n", "").replace("\r", "");
  }
}
