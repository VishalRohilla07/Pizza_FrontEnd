package com.pizza.ordering.repository;

import com.pizza.ordering.entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Pizza entity operations
 */
@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {

    /**
     * Find all available pizzas
     */
    List<Pizza> findByAvailable(Boolean available);

    /**
     * Find pizzas by category
     */
    List<Pizza> findByCategory(Pizza.PizzaCategory category);

    /**
     * Check if pizza name exists
     */
    boolean existsByName(String name);

    /**
     * Check if pizza name exists excluding specific ID (for updates)
     */
    boolean existsByNameAndIdNot(String name, Long id);
}
