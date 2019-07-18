package com.piccolomini.reactive.gateways.http;

import com.piccolomini.reactive.domains.Order;
import com.piccolomini.reactive.gateways.OrderStatusGateway;
import com.piccolomini.reactive.gateways.http.handlers.OrderStatusHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class OrderStatusFunctionalController {

  public final OrderStatusGateway orderStatusGateway;

  @Autowired
  public OrderStatusFunctionalController(OrderStatusGateway orderStatusGateway) {
    this.orderStatusGateway = orderStatusGateway;
  }

  @Bean
  RouterFunction<ServerResponse> routeOrderStatus(OrderStatusHandler orderStatusHandler) {
    return RouterFunctions.route(
        GET("/orderStatus").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
        orderStatusHandler::handle);
  }

  @Bean
  RouterFunction<ServerResponse> routeStatus() {
    return RouterFunctions.route(
        GET("/status").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
        this::handleStatus);
  }

  @Bean
  RouterFunction<ServerResponse> routeSimulateError() {
    return RouterFunctions.route(
        GET("/simulateGenericError").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
        this::handleSimulateError);
  }

  private Mono<ServerResponse> handleSimulateError(ServerRequest serverRequest) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(orderStatusGateway.simulateUncaughtError(), Order.class);
  }

  private Mono<ServerResponse> handleStatus(ServerRequest serverRequest) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(orderStatusGateway.getStatus(1L), Order.class);
  }
}
