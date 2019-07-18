package com.piccolomini.reactive.domains;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

  private String msg;
  private String cause;
}
