package com.pizza.ordering.dto;

import com.pizza.ordering.entity.Pizza;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response DTO for pizza data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PizzaResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Pizza.PizzaCategory category;
    private String imageUrl;
    private Boolean available;

    public static PizzaResponse fromEntity(Pizza pizza) {
        return new PizzaResponse(
                pizza.getId(),
                pizza.getName(),
                pizza.getDescription(),
                pizza.getPrice(),
                pizza.getCategory(),
                pizza.getImageUrl(),
                pizza.getAvailable());
    }
}
