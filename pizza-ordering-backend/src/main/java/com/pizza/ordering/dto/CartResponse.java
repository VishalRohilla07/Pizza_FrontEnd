package com.pizza.ordering.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response DTO for cart data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Long id;
    private List<CartItemResponse> items;
    private Integer totalItems;
    private BigDecimal totalPrice;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItemResponse {
        private Long id;
        private PizzaResponse pizza;
        private Integer quantity;
        private BigDecimal subtotal;
    }
}
