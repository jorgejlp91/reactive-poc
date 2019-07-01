package com.piccolomini.reactive.gateways.mongo;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.piccolomini.reactive.domains.Order;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderRepositoryIntegrationTest {

  private static final String TOKEN = "TOKENCARD123";
  @Autowired private OrderRepository repository;

  @Autowired private ReactiveMongoOperations operations;

  @Before
  public void setUp() {

    final Mono<MongoCollection<Document>> recreateCollection =
        operations
            .collectionExists(Order.class)
            .flatMap(exists -> exists ? operations.dropCollection(Order.class) : Mono.just(exists))
            .then(operations.createCollection(Order.class, CollectionOptions.empty()));

    StepVerifier.create(recreateCollection).expectNextCount(1).verifyComplete();

    final Flux<Order> insertAll =
        operations.insertAll(
            Flux.just(
                    new Order(1L, "Bicicleta", 1, TOKEN, BigDecimal.valueOf(500.00)),
                    new Order(2L, "Bola de Futebol", 5, TOKEN, BigDecimal.valueOf(53.90)),
                    new Order(3L, "Tenis kichute", 1, "TOKENCARD444", BigDecimal.valueOf(199.99)))
                .collectList());

    StepVerifier.create(insertAll).expectNextCount(3).verifyComplete();
  }

  @Test
  public void findOneByCodeShouldReturnOneProduct() {
    final Mono<Order> order = repository.findById(1L);
    StepVerifier.create(order).expectNextCount(1L).verifyComplete();
  }

  @Test
  public void findOneByInvalidCodehouldReturnEmpty() {
    final Mono<Order> order = repository.findById(99L);
    StepVerifier.create(order).expectNextCount(0L).verifyComplete();
  }

  @Test
  public void deleteAndFindOneOrderShouldOk() {
    final Flux<Order> deleteAndFind =
        repository.deleteById(Mono.just(2L)).thenMany(repository.findById(2L));

    StepVerifier.create(deleteAndFind).expectSubscription().expectNextCount(0L).expectComplete();
  }

  @Test
  public void saveNewOrderShouldOk() {
    final Mono<Order> newOrder =
        repository.save(new Order(8L, "Bicicleta", 1, TOKEN, BigDecimal.valueOf(1500.00)));
    StepVerifier.create(newOrder).expectNextCount(1).expectComplete();
  }
}
