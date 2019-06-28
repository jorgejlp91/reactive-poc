package com.piccolomini.reactive.config;

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
  public ResponseEntity<String> handleNotFound(final OrderNotFoundException ex) {
    log.error(ex.getMessage(), ex);
    // final ErrorResponse error = new ErrorResponse();
    // error.setMsg("not found");
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }
}
