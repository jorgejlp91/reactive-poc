package com.piccolomini.nonreactive.gateways.mongo;

import com.piccolomini.nonreactive.domains.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, Long> {}
