package com.piccolomini.reactive.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Table("account")
@NoArgsConstructor
@AllArgsConstructor
public class Account {

  @Id private Integer user_id;
  @NotNull private String username;
  @NotNull private String password;
  @NotNull private String email;
}
