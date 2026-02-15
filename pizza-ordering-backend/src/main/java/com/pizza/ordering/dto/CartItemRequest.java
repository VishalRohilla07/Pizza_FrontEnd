package com.pizza.ordering.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for adding/updating cart items
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {

    @NotNull(message = "Pizza ID is required")
    private Long pizzaId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity = 1;
}
