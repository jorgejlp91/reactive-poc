package com.piccolomini.reactive.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Getter
@Setter
@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

  private HttpStatus status = HttpStatus.BAD_REQUEST;
  private String message = "please provide a name";

  public GlobalErrorAttributes() {
    super(false);
  }

  @Override
  public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
    Map<String, Object> map = super.getErrorAttributes(request, includeStackTrace);
    map.put("status", getStatus());
    map.put("message", getMessage());
    return map;
  }
}
