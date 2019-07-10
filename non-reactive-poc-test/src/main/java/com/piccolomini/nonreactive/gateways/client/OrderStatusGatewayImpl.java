package com.piccolomini.nonreactive.gateways.client;

import com.piccolomini.nonreactive.gateways.OrderStatusGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class OrderStatusGatewayImpl implements OrderStatusGateway {

  private final RestTemplate client;

  @Value("${inventory.uri:https://www.random.org}")
  private String orderUri;

  @Value("${inventory.endpoint:/integers/?num=1&min=1&max=3&col=1&base=10&format=plain}")
  private String orderEndpoint;

  @Autowired
  public OrderStatusGatewayImpl(final RestTemplate restTemplate) {
    this.client = restTemplate;
  }

  @Override
  public Long getStatus(final Long orderId) {
    String rawStatus = getRandomAsString(orderUri + orderEndpoint);
    log.info("Get order status: {} | order: {}", rawStatus, orderId);
    return Long.valueOf(sanitize(rawStatus));
  }

  private String getRandomAsString(final String url) {
    return client.getForObject(url, String.class);
  }
}
