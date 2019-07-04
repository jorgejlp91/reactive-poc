package com.piccolomini.reactive;

import com.piccolomini.reactive.gateways.postgres.AccountRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

// @ComponentScan(
//    basePackages = "com.piccolomini.reactive",
//    excludeFilters =
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes =
// AccountRepository.class))
@SpringBootApplication
// @EnableJdbcRepositories
// @EnableR2dbcRepositories(
//    excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OrderRepository.class))
@EnableMongoRepositories(
    excludeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AccountRepository.class))
public class ReactivePocApplication {

  public static void main(String[] args) {
    SpringApplication.run(ReactivePocApplication.class, args);
  }
}
