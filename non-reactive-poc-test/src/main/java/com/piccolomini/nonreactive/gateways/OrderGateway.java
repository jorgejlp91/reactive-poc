package com.piccolomini.nonreactive.gateways;

import com.piccolomini.nonreactive.domains.Order;

import java.util.List;

public interface OrderGateway {

  List<Order> findAll();

  Order save(Order order);

  Order findOne(Long id);

  void deleteOne(Long id);
}
