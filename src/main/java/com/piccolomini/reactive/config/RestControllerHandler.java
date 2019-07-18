package com.piccolomini.reactive.config;

import com.piccolomini.reactive.domains.ErrorResponse;
import com.piccolomini.reactive.domains.exceptions.AccountNotFoundException;
import com.piccolomini.reactive.domains.exceptions.OrderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestControllerHandler {

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(final OrderNotFoundException ex) {
    log.error(ex.getMessage(), ex);
    final ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setMsg("order not found");
    errorResponse.setCause(ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(AccountNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(final AccountNotFoundException ex) {
    log.error(ex.getMessage(), ex);
    final ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setMsg("account not found");
    errorResponse.setCause(ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  // this not works for a exception thrown outside flux/mono stream or
  // an uncaught exception
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(final Exception ex) {
    log.error(ex.getMessage(), ex);

    final ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setMsg("an unexpected error was occurred");
    errorResponse.setCause(ex.getMessage());

    return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
  }
}
