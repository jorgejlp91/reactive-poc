package com.piccolomini.reactive.gateways.client;

import com.piccolomini.reactive.domains.Order;
import com.piccolomini.reactive.gateways.OrderStatusGateway;
import com.piccolomini.reactive.gateways.client.json.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class OrderStatusGatewayImpl implements OrderStatusGateway {

  private final WebClient client;

  @Value("${integration.uri:http://localhost:8084}")
  private String orderUri;

  @Value("${integration.endpoint:/integration/api/v1/status}")
  private String orderEndpoint;

  @Autowired
  public OrderStatusGatewayImpl(final WebClient.Builder webClientBuilder) {
    this.client = webClientBuilder.baseUrl(orderUri).build();
  }

  @Override
  public Mono<Order> getStatus(final Long orderId) {
    return client
        .post()
        .uri(orderUri + orderEndpoint)
        .syncBody(buildBody(orderId))
        .retrieve()
        .bodyToMono(OrderStatus.class)
        .flatMap(
            orderStatus -> {
              log.info("Status retrieved : {}", orderStatus);
              Order order = new Order();
              order.setStatus(orderStatus.getStatus());
              return Mono.just(order);
            });
  }

  @Override
  public Mono<Order> simulateUncaughtError() {
    return client
        .post()
        .uri(orderUri + "bla" + orderEndpoint)
        .retrieve()
        .bodyToMono(OrderStatus.class)
        .flatMap(orderStatus -> Mono.empty());
  }

  private OrderStatus buildBody(final Long orderId) {
    OrderStatus body = new OrderStatus();
    body.setOrderId(orderId);
    return body;
  }
}
