package com.pizza.ordering.repository;

import com.pizza.ordering.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Order entity operations
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find all orders for a specific user
     */
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * Find orders by status with pagination
     */
    Page<Order> findByOrderStatus(Order.OrderStatus orderStatus, Pageable pageable);

    /**
     * Find order by payment intent ID
     */
    Optional<Order> findByPaymentIntentId(String paymentIntentId);

    /**
     * Find all orders with pagination, ordered by creation date descending
     */
    Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
