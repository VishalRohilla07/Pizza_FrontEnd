package com.pizza.ordering.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order entity representing customer orders
 */
@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_order_status", columnList = "orderStatus"),
        @Index(name = "idx_payment_status", columnList = "paymentStatus"),
        @Index(name = "idx_created_at", columnList = "createdAt")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus = OrderStatus.PLACED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(unique = true)
    private String paymentIntentId; // Stripe payment intent ID

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Order status lifecycle
     */
    public enum OrderStatus {
        PLACED,
        CONFIRMED,
        PREPARING,
        OUT_FOR_DELIVERY,
        DELIVERED,
        CANCELLED
    }

    /**
     * Payment status
     */
    public enum PaymentStatus {
        PENDING,
        SUCCESS,
        FAILED
    }

    /**
     * Add item to order
     */
    public void addItem(OrderItem item) {
        item.setOrder(this);
        orderItems.add(item);
    }

    /**
     * Check if order can be cancelled
     */
    public boolean canBeCancelled() {
        return orderStatus == OrderStatus.PLACED || orderStatus == OrderStatus.CONFIRMED;
    }

    /**
     * Check if status transition is valid
     */
    public boolean canTransitionTo(OrderStatus newStatus) {
        if (orderStatus == OrderStatus.CANCELLED || orderStatus == OrderStatus.DELIVERED) {
            return false; // Terminal states
        }

        // Define valid transitions
        return switch (orderStatus) {
            case PLACED -> newStatus == OrderStatus.CONFIRMED || newStatus == OrderStatus.CANCELLED;
            case CONFIRMED -> newStatus == OrderStatus.PREPARING || newStatus == OrderStatus.CANCELLED;
            case PREPARING -> newStatus == OrderStatus.OUT_FOR_DELIVERY;
            case OUT_FOR_DELIVERY -> newStatus == OrderStatus.DELIVERED;
            default -> false;
        };
    }
}
