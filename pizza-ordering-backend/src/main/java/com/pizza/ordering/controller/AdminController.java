package com.pizza.ordering.controller;

import com.pizza.ordering.dto.ApiResponse;
import com.pizza.ordering.dto.OrderResponse;
import com.pizza.ordering.entity.Order;
import com.pizza.ordering.service.OrderService;
import com.pizza.ordering.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for admin operations
 */
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private WebSocketService webSocketService;

    /**
     * Get all orders with pagination
     */
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Order.OrderStatus status) {

        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponse> orders;

        if (status != null) {
            orders = orderService.getOrdersByStatus(status, pageable);
        } else {
            orders = orderService.getAllOrders(pageable);
        }

        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    /**
     * Update order status
     */
    @PutMapping("/orders/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam Order.OrderStatus status) {

        OrderResponse order = orderService.updateOrderStatus(id, status);

        // Send real-time update via WebSocket
        webSocketService.sendOrderUpdate(id, order.getUserId(), status);
        webSocketService.broadcastOrderUpdate(id, status);

        return ResponseEntity.ok(ApiResponse.success(order, "Order status updated"));
    }
}
