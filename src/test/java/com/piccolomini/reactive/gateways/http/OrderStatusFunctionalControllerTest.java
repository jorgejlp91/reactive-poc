package com.piccolomini.reactive.gateways.http;

import com.piccolomini.reactive.domains.Order;
import com.piccolomini.reactive.gateways.http.handlers.OrderStatusHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = OrderStatusFunctionalController.class)
public class OrderStatusFunctionalControllerTest {

  @Autowired private WebTestClient client;

  @MockBean private OrderStatusHandler handler;

  @Test
  public void testGetOrderStatusShouldBeOk() {
    // GIVEN: A valid mock of gateway
    final Order order = new Order(1L, "Bicicleta", 1, "TOKENCARD", BigDecimal.valueOf(1500.00));
    given(handler.listOrderStatus(BDDMockito.any(ServerRequest.class)))
        .willReturn(
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(order), Order.class));

    // WHEN: the api is called
    client
        .get()
        .uri("/orderStatus")
        .exchange()

        // THEN: I expect a 200 http status code
        .expectStatus()
        .isOk();
  }
}
