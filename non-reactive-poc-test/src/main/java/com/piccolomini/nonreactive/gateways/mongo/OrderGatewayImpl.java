package com.piccolomini.nonreactive.gateways.mongo;

import com.piccolomini.nonreactive.domains.Order;
import com.piccolomini.nonreactive.domains.exceptions.OrderNotFoundException;
import com.piccolomini.nonreactive.gateways.OrderGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderGatewayImpl implements OrderGateway {

  private final OrderRepository repository;

  @Autowired
  public OrderGatewayImpl(OrderRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<Order> findAll() {
    return repository.findAll();
  }

  @Override
  public Order save(Order order) {
    return repository.save(order);
  }

  @Override
  public Order findOne(Long id) {
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      // DO nothing
    }
    return repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
  }

  @Override
  public void deleteOne(Long id) {
    repository.deleteById(id);
  }
}
