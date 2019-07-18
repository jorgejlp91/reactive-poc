package com.piccolomini.nonreactive.gateways.client;

import com.piccolomini.nonreactive.gateways.OrderStatusGateway;
import com.piccolomini.nonreactive.gateways.client.json.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Component
public class OrderStatusGatewayImpl implements OrderStatusGateway {

  private final RestTemplate client;

  @Value("${integration.uri:http://localhost:8084}")
  private String orderUri;

  @Value("${integration.endpoint:/integration/api/v1/status}")
  private String orderEndpoint;

  @Autowired
  public OrderStatusGatewayImpl(final RestTemplate restTemplate) {
    this.client = restTemplate;
  }

  @Override
  public String getStatus(final Long orderId) {

    OrderStatus orderStatus =
        Optional.ofNullable(
                client.postForObject(
                    orderUri + orderEndpoint, buildBody(orderId), OrderStatus.class))
            .orElse(new OrderStatus());
    log.info("Get order status: {} | order: {}", orderStatus.getStatus(), orderId);
    return orderStatus.getStatus();
  }

  private OrderStatus buildBody(final Long orderId) {
    OrderStatus body = new OrderStatus();
    body.setOrderId(orderId);
    return body;
  }
}
