package com.piccolomini.reactive.gateways.client;

import com.piccolomini.reactive.gateways.OrderStatusGateway;
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

  @Value("${inventory.uri:https://www.random.org}")
  private String orderUri;

  @Value("${inventory.endpoint:/integers/?num=1&min=1&max=3&col=1&base=10&format=plain}")
  private String orderEndpoint;

  @Autowired
  public OrderStatusGatewayImpl(final WebClient.Builder webClientBuilder) {
    this.client = webClientBuilder.baseUrl(orderUri).build();
  }

  @Override
  public Mono<Long> getStatus(final Long orderId) {
    return getRandomAsString(orderUri + orderEndpoint)
        .map(c -> Long.valueOf(sanitize(c)))
        .doOnNext(c -> log.info("Get order status: {} | order: {}", c, orderId));
  }

  private Mono<String> getRandomAsString(final String url) {
    return client
        .get()
        .uri(url)
        .retrieve()
        .bodyToMono(String.class)
        .defaultIfEmpty("0")
        // fallback
        .onErrorReturn("0");
  }
}
