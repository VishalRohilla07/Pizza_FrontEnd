package com.pizza.ordering.repository;

import com.pizza.ordering.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for CartItem entity operations
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * Find cart item by cart ID and pizza ID
     */
    Optional<CartItem> findByCartIdAndPizzaId(Long cartId, Long pizzaId);

    /**
     * Delete all cart items for a specific cart
     */
    void deleteByCartId(Long cartId);
}
