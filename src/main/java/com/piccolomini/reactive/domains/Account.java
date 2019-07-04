package com.piccolomini.reactive.domains;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Table("account")
public class Account {

  @Id private Integer user_id;
  @NotNull private String username;
  @NotNull private String password;
  @NotNull private String email;
}
