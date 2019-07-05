package com.piccolomini.reactive.config;

import com.piccolomini.reactive.gateways.postgres.AccountRepository;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.function.DatabaseClient;
import org.springframework.data.r2dbc.function.ReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;

@Configuration
public class PostgresConfig extends AbstractR2dbcConfiguration {

  @Primary
  @Bean
  public AccountRepository repository(R2dbcRepositoryFactory factory) {
    return factory.getRepository(AccountRepository.class);
  }

  @Bean
  public R2dbcRepositoryFactory factory(
      DatabaseClient client,
      RelationalMappingContext context,
      ReactiveDataAccessStrategy strategy) {
    context.afterPropertiesSet();
    return new R2dbcRepositoryFactory(client, context, strategy);
  }

  @Bean
  public ConnectionFactory connectionFactory() {
    return new PostgresqlConnectionFactory(
        PostgresqlConnectionConfiguration.builder()
            .host("localhost")
            .port(5432)
            .username("root")
            .password("pass123")
            .database("root")
            .build());
  }
}
