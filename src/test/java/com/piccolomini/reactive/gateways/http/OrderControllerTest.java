package com.piccolomini.reactive.gateways.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piccolomini.reactive.domains.Order;
import com.piccolomini.reactive.gateways.OrderGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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

  @Autowired private ObjectMapper objectMapper;

  @Test
  public void testGetOrderByCodeShouldBeOk() {
    // GIVEN: A valid mock of gateway
    final Order order = new Order(1L, "Bicicleta", 1, "TOKENCARD", BigDecimal.valueOf(1500.00));
    given(gateway.findOne(1L)).willReturn(Mono.just(order));

    // WHEN: the api is called
    client
        .get()
        .uri("/orders/1")
        .exchange()

        // THEN: I expect a 200 http status code
        .expectStatus()
        .isOk()

        // AND: a valid json body
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
    // GIVEN: a valid empty mock of gateway
    given(gateway.findOne(99L)).willReturn(Mono.empty());

    // WHEN: the api is called
    client
        .get()
        .uri("/orders/99")
        .exchange()

        // THEN: the http status code is 404
        .expectStatus()
        .isNotFound();
  }

  @Test
  public void testPostValidOrderShouldReturnCreate() throws JsonProcessingException {
    // GIVEN: a valid mock of gateway
    final Order order = new Order(1L, "Bicicleta", 1, "TOKENCARD", BigDecimal.valueOf(1500.00));
    given(gateway.save(BDDMockito.any(Order.class))).willReturn(Mono.just(order));

    // WHEN: the api is called
    client
        .post()
        .uri("/orders")
        .body(BodyInserters.fromObject(order))
        .exchange()

        // THEN: the http status code is 201 with valid header
        .expectStatus()
        .isCreated()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON_UTF8)

        // AND: the response body is valid
        .expectBody()
        .json(objectMapper.writeValueAsString(order));
  }

  @Test
  public void testPostInvalidOrderShouldReturnBadRequest() {
    // GIVEN: an invalid valid request
    final Order order = new Order();
    order.setId(5L);

    // WHEN: the api is called
    client
        .post()
        .uri("/orders")
        .body(BodyInserters.fromObject(order))
        .exchange()

        // THEN: I expect a 400 http status code
        .expectStatus()
        .isBadRequest();
  }

  @Test
  public void testDeleteValidOrderShouldReturnAccept() {
    // GIVEN: a valid gateway mock
    given(gateway.deleteOne(4L)).willReturn(Mono.empty());

    // WHEN: the api is called
    client
        .delete()
        .uri("/orders/4")
        .exchange()

        // THEN: the http status code is 202
        .expectStatus()
        .isAccepted();
  }
}
