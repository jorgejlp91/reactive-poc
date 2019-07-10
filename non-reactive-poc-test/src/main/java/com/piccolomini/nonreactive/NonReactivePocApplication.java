package com.piccolomini.nonreactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class NonReactivePocApplication {

  public static void main(String[] args) {
    SpringApplication.run(NonReactivePocApplication.class, args);
  }
}
