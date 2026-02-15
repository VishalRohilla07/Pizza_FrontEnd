package com.pizza.ordering.dto;

import com.pizza.ordering.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for order data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private Long userId;
    private List<OrderItemResponse> items;
    private BigDecimal totalAmount;
    private Order.OrderStatus orderStatus;
    private Order.PaymentStatus paymentStatus;
    private String paymentIntentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponse {
        private Long id;
        private PizzaResponse pizza;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal subtotal;
    }
}
