package com.piccolomini.nonreactive.gateways;

public interface OrderStatusGateway {

  Long getStatus(Long orderId);

  default String sanitize(final String content) {
    return content.replace("\n", "").replace("\r", "");
  }
}
