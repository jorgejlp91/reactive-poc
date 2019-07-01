package com.piccolomini.reactive.gateways.mongo;

import com.piccolomini.reactive.domains.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OrderGatewayImplTest {

  @Mock private OrderRepository orderRepository;

  @InjectMocks private OrderGatewayImpl orderGateway;

  @Captor private ArgumentCaptor<Long> captor;

  @Test
  public void shouldSaveSuccessfully() {
    // GIVEN: a valid repository mock
    Order order = new Order(8L, "Bicicleta", 1, "TOKEN", BigDecimal.valueOf(1500.00));
    BDDMockito.given(orderRepository.save(order)).willReturn(Mono.just(order));

    // WHEN: the gateway is called
    Mono<Order> save = orderGateway.save(order);

    // THEN: the response is valid
    StepVerifier.create(save).expectNextCount(1).expectNext(order);
  }

  @Test
  public void deleteOne() {
    // GIVEN: a valid repository mock
    BDDMockito.given(orderRepository.deleteById(BDDMockito.anyLong())).willReturn(Mono.empty());

    // WHEN: the gateway is called
    orderGateway.deleteOne(8L);

    // THEN: the parameter passed to repository was the same that the gateway received
    verify(orderRepository).deleteById(captor.capture());
    assertThat(captor.getValue(), equalTo(8L));
  }

  @Test
  public void shouldFindOrderSuccessfully() {
    // GIVEN: a valid repository mock
    Order order = new Order(8L, "Bicicleta", 1, "TOKEN", BigDecimal.valueOf(1500.00));
    BDDMockito.given(orderRepository.findById(captor.capture())).willReturn(Mono.just(order));

    // WHEN: the gateway is called
    Mono<Order> find = orderGateway.findOne(8L);

    // THEN: i expect to receive a valid Order
    StepVerifier.create(find)
        .expectNextMatches(
            findOne -> {
              assertThat(findOne.getProductName(), equalTo("Bicicleta"));
              assertThat(captor.getValue(), equalTo(8L));
              return true;
            })
        .verifyComplete();
  }

  @Test
  public void findAll() {
    // GIVEN: a valid repository mock
    Order order1 = new Order(8L, "Bicicleta", 1, "TOKEN", BigDecimal.valueOf(1500.00));
    Order order2 = new Order(4L, "Capacete", 1, "TOKEN", BigDecimal.valueOf(180.99));
    BDDMockito.given(orderRepository.findAll()).willReturn(Flux.just(order1, order2));

    // WHEN: the gateway is called
    Flux<Order> findALl = orderGateway.findAll();

    // THEN: i expect to receive a valid list of Orders
    List<Order> orders = findALl.collectList().block();
    assertTrue(orders.stream().anyMatch(x -> order1.getId().equals(x.getId())));
    assertTrue(orders.stream().anyMatch(x -> order2.getId().equals(x.getId())));
  }
}
