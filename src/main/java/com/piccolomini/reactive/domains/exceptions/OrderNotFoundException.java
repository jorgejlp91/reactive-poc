package com.piccolomini.reactive.domains.exceptions;

public class OrderNotFoundException extends RuntimeException {

  public OrderNotFoundException(final Long id) {
    super(String.format("order[%s].notFound", id));
  }
}
