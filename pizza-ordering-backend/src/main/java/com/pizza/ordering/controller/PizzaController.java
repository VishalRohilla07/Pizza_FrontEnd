package com.pizza.ordering.controller;

import com.pizza.ordering.dto.ApiResponse;
import com.pizza.ordering.dto.PizzaRequest;
import com.pizza.ordering.dto.PizzaResponse;
import com.pizza.ordering.service.PizzaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for pizza management endpoints
 */
@RestController
@RequestMapping("/pizzas")
public class PizzaController {

    @Autowired
    private PizzaService pizzaService;

    /**
     * Get all available pizzas (public)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PizzaResponse>>> getAllPizzas() {
        List<PizzaResponse> pizzas = pizzaService.getAvailablePizzas();
        return ResponseEntity.ok(ApiResponse.success(pizzas));
    }

    /**
     * Get pizza by ID (public)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PizzaResponse>> getPizzaById(@PathVariable Long id) {
        PizzaResponse pizza = pizzaService.getPizzaById(id);
        return ResponseEntity.ok(ApiResponse.success(pizza));
    }

    /**
     * Create new pizza (Admin only)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PizzaResponse>> createPizza(@Valid @RequestBody PizzaRequest request) {
        PizzaResponse pizza = pizzaService.createPizza(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(pizza, "Pizza created successfully"));
    }

    /**
     * Update pizza (Admin only)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PizzaResponse>> updatePizza(
            @PathVariable Long id,
            @Valid @RequestBody PizzaRequest request) {
        PizzaResponse pizza = pizzaService.updatePizza(id, request);
        return ResponseEntity.ok(ApiResponse.success(pizza, "Pizza updated successfully"));
    }

    /**
     * Delete pizza (Admin only)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePizza(@PathVariable Long id) {
        pizzaService.deletePizza(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Pizza deleted successfully"));
    }
}
