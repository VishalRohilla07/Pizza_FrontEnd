package com.pizza.ordering.dto;

import com.pizza.ordering.entity.Pizza;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for creating/updating pizzas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PizzaRequest {

    @NotBlank(message = "Pizza name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Category is required")
    private Pizza.PizzaCategory category;

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    private Boolean available = true;
}
