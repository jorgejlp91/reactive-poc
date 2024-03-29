package com.piccolomini.reactive;

import com.piccolomini.reactive.gateways.postgres.AccountRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@SpringBootApplication
@EnableMongoRepositories(
    excludeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AccountRepository.class))
@EnableSwagger2WebFlux
public class ReactivePocApplication {

  public static void main(String[] args) {
    SpringApplication.run(ReactivePocApplication.class, args);
  }
}
