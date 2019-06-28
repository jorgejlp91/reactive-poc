package com.piccolomini.reactive.http;

import com.piccolomini.reactive.domains.Order;
import com.piccolomini.reactive.gateways.OrderGateway;
import com.piccolomini.reactive.gateways.http.OrderController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = OrderController.class)
public class OrderControllerTest {

  @Autowired WebTestClient client;

  @MockBean OrderGateway gateway;

  @Test
  public void testGetOrderByCodeShouldBeOk() {
    final Order order = new Order(1L, "Bicicleta", 1, "TOKENCARD", BigDecimal.valueOf(1500.00));

    given(gateway.findOne(1L)).willReturn(Mono.just(order));

    client
        .get()
        .uri("/orders/1")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id")
        .isEqualTo(1)
        .jsonPath("$.productName")
        .isEqualTo("Bicicleta")
        .jsonPath("$.quantity")
        .isEqualTo(1)
        .jsonPath("$.cardToken")
        .isEqualTo("TOKENCARD")
        .jsonPath("$.unitPrice")
        .isEqualTo(1500.00);
  }

  @Test
  public void testGetInvalidOrderShouldReturnNotFound() {
    given(gateway.findOne(99L)).willReturn(Mono.empty());

    client.get().uri("/orders/99").exchange().expectStatus().isNotFound();
  }

  @Test
  public void testPostValidOrderShouldReturnCreate() {
    final Order order = new Order(1L, "Bicicleta", 1, "TOKENCARD", BigDecimal.valueOf(1500.00));

    given(gateway.save(BDDMockito.any(Order.class))).willReturn(Mono.just(order));

    client
        .post()
        .uri("/orders")
        .body(BodyInserters.fromObject(order))
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody()
        .jsonPath("$.id")
        .isEqualTo(1)
        .jsonPath("$.productName")
        .isEqualTo("Bicicleta")
        .jsonPath("$.quantity")
        .isEqualTo(1)
        .jsonPath("$.cardToken")
        .isEqualTo("TOKENCARD")
        .jsonPath("$.unitPrice")
        .isEqualTo(1500.00);
  }

  @Test
  public void testPostInvalidOrderShouldReturnBadRequest() {
    final Order order = new Order();
    order.setId(5L);

    client
        .post()
        .uri("/orders")
        .body(BodyInserters.fromObject(order))
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  public void testDeleteValidOrderShouldReturnAccept() {
    given(gateway.deleteOne(4L)).willReturn(Mono.empty());

    client.delete().uri("/orders/4").exchange().expectStatus().isAccepted();
  }
}
