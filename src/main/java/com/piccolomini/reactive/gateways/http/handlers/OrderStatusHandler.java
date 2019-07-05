package com.piccolomini.reactive.gateways.http.handlers;

import com.piccolomini.reactive.domains.Order;
import com.piccolomini.reactive.usecases.GetOrderStatusUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Component
public class OrderStatusHandler {

  private final GetOrderStatusUseCase getOrderStatusUseCase;

  @Autowired
  public OrderStatusHandler(GetOrderStatusUseCase getOrderStatusUseCase) {
    this.getOrderStatusUseCase = getOrderStatusUseCase;
  }

  public Mono<ServerResponse> handle(ServerRequest request) {
    return ok().contentType(MediaType.APPLICATION_JSON)
        .body(getOrderStatusUseCase.execute(request), Order.class)
        .switchIfEmpty(ServerResponse.notFound().build())
        .onErrorResume(
            e ->
                Mono.just("Error " + e.getMessage())
                    .flatMap(
                        s ->
                            ServerResponse.unprocessableEntity()
                                .contentType(MediaType.TEXT_PLAIN)
                                .syncBody(s)));
  }
}
