package com.piccolomini.nonreactive.gateways.http;

import com.piccolomini.nonreactive.domains.Order;
import com.piccolomini.nonreactive.gateways.OrderGateway;
import com.piccolomini.nonreactive.gateways.OrderStatusGateway;
import com.piccolomini.nonreactive.usecases.GetOrderStatusUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class OrderController {

  private final GetOrderStatusUseCase usecase;
  private final OrderGateway gateway;
  private final OrderStatusGateway orderStatusGateway;

  @Autowired
  public OrderController(
      GetOrderStatusUseCase usecase, OrderGateway gateway, OrderStatusGateway orderStatusGateway) {
    this.usecase = usecase;
    this.gateway = gateway;
    this.orderStatusGateway = orderStatusGateway;
  }

  @GetMapping("/orderStatus")
  @ResponseStatus(HttpStatus.OK)
  public List<Order> findAll() {
    return usecase.execute();
  }

  @GetMapping("/status")
  @ResponseStatus(HttpStatus.OK)
  public String findStatus() {
    return orderStatusGateway.getStatus(1L);
  }

  @PostMapping("/orders")
  @ResponseStatus(HttpStatus.CREATED)
  public Order create(@Valid @RequestBody final Order order) {
    return gateway.save(order);
  }

  @PutMapping("/orders")
  @ResponseStatus(HttpStatus.OK)
  public Order update(@Valid @RequestBody final Order order) {
    return gateway.save(order);
  }

  @GetMapping("/orders/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Order find(@PathVariable final Long id) {
    return gateway.findOne(id);
  }

  @DeleteMapping("/orders/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void delete(@PathVariable final Long id) {
    gateway.deleteOne(id);
  }
}
