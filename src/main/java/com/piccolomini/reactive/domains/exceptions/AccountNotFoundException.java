package com.piccolomini.reactive.domains.exceptions;

public class AccountNotFoundException extends RuntimeException {

  public AccountNotFoundException(final String username) {
    super(String.format("account [%s] not found", username));
  }
}
