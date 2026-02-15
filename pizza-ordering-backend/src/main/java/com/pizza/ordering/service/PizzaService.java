package com.pizza.ordering.service;

import com.pizza.ordering.dto.PizzaRequest;
import com.pizza.ordering.dto.PizzaResponse;
import com.pizza.ordering.entity.Pizza;
import com.pizza.ordering.exception.BadRequestException;
import com.pizza.ordering.exception.ResourceNotFoundException;
import com.pizza.ordering.repository.PizzaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for pizza management operations
 */
@Service
public class PizzaService {

    private static final Logger logger = LoggerFactory.getLogger(PizzaService.class);

    @Autowired
    private PizzaRepository pizzaRepository;

    /**
     * Get all pizzas
     */
    public List<PizzaResponse> getAllPizzas() {
        logger.debug("Fetching all pizzas");
        return pizzaRepository.findAll().stream()
                .map(PizzaResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get only available pizzas
     */
    public List<PizzaResponse> getAvailablePizzas() {
        logger.debug("Fetching available pizzas");
        return pizzaRepository.findByAvailable(true).stream()
                .map(PizzaResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get pizza by ID
     */
    public PizzaResponse getPizzaById(Long id) {
        logger.debug("Fetching pizza with ID: {}", id);
        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pizza", "id", id));
        return PizzaResponse.fromEntity(pizza);
    }

    /**
     * Create new pizza (Admin only)
     */
    @Transactional
    public PizzaResponse createPizza(PizzaRequest request) {
        logger.info("Creating new pizza: {}", request.getName());

        // Check for duplicate name
        if (pizzaRepository.existsByName(request.getName())) {
            throw new BadRequestException("Pizza with name '" + request.getName() + "' already exists");
        }

        Pizza pizza = new Pizza();
        pizza.setName(request.getName());
        pizza.setDescription(request.getDescription());
        pizza.setPrice(request.getPrice());
        pizza.setCategory(request.getCategory());
        pizza.setImageUrl(request.getImageUrl());
        pizza.setAvailable(request.getAvailable());

        pizza = pizzaRepository.save(pizza);
        logger.info("Pizza created with ID: {}", pizza.getId());

        return PizzaResponse.fromEntity(pizza);
    }

    /**
     * Update pizza (Admin only)
     */
    @Transactional
    public PizzaResponse updatePizza(Long id, PizzaRequest request) {
        logger.info("Updating pizza with ID: {}", id);

        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pizza", "id", id));

        // Check for duplicate name (excluding current pizza)
        if (pizzaRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new BadRequestException("Pizza with name '" + request.getName() + "' already exists");
        }

        pizza.setName(request.getName());
        pizza.setDescription(request.getDescription());
        pizza.setPrice(request.getPrice());
        pizza.setCategory(request.getCategory());
        pizza.setImageUrl(request.getImageUrl());
        pizza.setAvailable(request.getAvailable());

        pizza = pizzaRepository.save(pizza);
        logger.info("Pizza updated: {}", pizza.getId());

        return PizzaResponse.fromEntity(pizza);
    }

    /**
     * Delete pizza (Admin only)
     */
    @Transactional
    public void deletePizza(Long id) {
        logger.info("Deleting pizza with ID: {}", id);

        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pizza", "id", id));

        // In production, you might want to check if pizza is in any pending orders
        // For now, we'll just delete it
        pizzaRepository.delete(pizza);
        logger.info("Pizza deleted: {}", id);
    }
}
