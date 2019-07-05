package com.piccolomini.reactive.gateways.http;

import com.piccolomini.reactive.gateways.http.handlers.OrderStatusHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class OrderStatusFunctionalController {

  @Bean
  RouterFunction<ServerResponse> route(OrderStatusHandler orderStatusHandler) {
    return RouterFunctions.route(
        GET("/orderStatus").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
        orderStatusHandler::handle);
  }
}
