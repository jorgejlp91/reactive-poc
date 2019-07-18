package com.piccolomini.reactive.gateways.client.json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderStatus {

  private Long orderId;
  private String status;
}
