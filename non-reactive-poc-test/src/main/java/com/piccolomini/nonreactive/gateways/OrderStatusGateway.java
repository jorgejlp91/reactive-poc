package com.piccolomini.nonreactive.gateways;

public interface OrderStatusGateway {

  String getStatus(Long orderId);
}
