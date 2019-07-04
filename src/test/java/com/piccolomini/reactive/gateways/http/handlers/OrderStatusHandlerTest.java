package com.piccolomini.reactive.gateways.http.handlers;

import com.piccolomini.reactive.domains.Order;
import com.piccolomini.reactive.usecases.GetOrderStatusUseCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.reactive.function.server.EntityResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.isNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OrderStatusHandlerTest {

  //  @Mock private OrderGateway orderGateway;
  //  @Mock private OrderStatusGateway orderStatusGateway;
  @Mock private GetOrderStatusUseCase useCase;
  @InjectMocks private OrderStatusHandler orderStatusHandler;
  @Mock private ServerRequest request;

  @Test
  public void shouldReturnAValidResponse() {
    // GIVEN: a valid gateways mock
    Order order = new Order(8L, "Bicicleta", 1, "TOKEN", BigDecimal.valueOf(1500.00));
    order.setStatus("PAID");
    //    BDDMockito.given(orderGateway.findAll()).willReturn(Flux.just(order));
    //    BDDMockito.given(orderStatusGateway.getStatus(8L)).willReturn(Mono.just(1L));
    BDDMockito.given(useCase.execute(request)).willReturn(Flux.just(order));

    // WHEN: the handler is called
    Mono<ServerResponse> response = orderStatusHandler.listOrderStatus(request);

    // THEN: the response is valid
    StepVerifier.create(response)
        .expectNextCount(1)
        .assertNext(
            serverResponse -> {
              assertThat(serverResponse, instanceOf(EntityResponse.class));

              EntityResponse<Order> entityServerResponse = (EntityResponse<Order>) serverResponse;
              assertThat(entityServerResponse.statusCode(), equalTo(HttpStatus.OK));
              assertThat(entityServerResponse.entity(), isNotNull());
              assertThat(entityServerResponse.entity().getId(), equalTo(8L));
              assertThat(entityServerResponse.entity().getStatus(), equalTo("PAID"));
            });
  }
}
