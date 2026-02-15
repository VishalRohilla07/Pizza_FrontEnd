package com.pizza.ordering.repository;

import com.pizza.ordering.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Cart entity operations
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Find cart by user ID
     */
    Optional<Cart> findByUserId(Long userId);

    /**
     * Delete cart by user ID
     */
    void deleteByUserId(Long userId);
}
