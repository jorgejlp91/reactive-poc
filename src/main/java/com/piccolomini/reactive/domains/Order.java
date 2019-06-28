package com.piccolomini.reactive.domains;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
public class Order {

  @NotNull @Id private Long id;
  @NotBlank private String productName;
  @NotNull private Integer quantity;
  @NotBlank private String cardToken;
  @NotNull private BigDecimal unitPrice;
}
