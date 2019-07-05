package com.piccolomini.reactive.gateways.mongo;

import com.piccolomini.reactive.domains.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {}
