package com.piccolomini.nonreactive.domains;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Order {

  @NonNull @NotNull @Id private Long id;

  @NonNull @NotBlank private String productName;

  @NonNull @NotNull private Integer quantity;
  @NonNull @NotBlank private String cardToken;
  @NonNull @NotNull private BigDecimal unitPrice;
  private String status;

  public Order(Long id) {
    this.id = id;
  }

  public Order(Order order, String status) {
    this.id = order.getId();
    this.cardToken = order.getCardToken();
    this.productName = order.getProductName();
    this.quantity = order.getQuantity();
    this.unitPrice = order.getUnitPrice();
    this.status = status;
  }
}
