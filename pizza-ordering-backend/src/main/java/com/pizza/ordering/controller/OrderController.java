package com.pizza.ordering.controller;

import com.pizza.ordering.dto.ApiResponse;
import com.pizza.ordering.dto.OrderResponse;
import com.pizza.ordering.entity.User;
import com.pizza.ordering.repository.UserRepository;
import com.pizza.ordering.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for order operations
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create order from cart
     */
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(Authentication authentication) {
        Long userId = getUserId(authentication);
        OrderResponse order = orderService.createOrder(userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(order, "Order placed successfully"));
    }

    /**
     * Get current user's orders
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getUserOrders(Authentication authentication) {
        Long userId = getUserId(authentication);
        List<OrderResponse> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    /**
     * Get order by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(
            @PathVariable Long id,
            Authentication authentication) {
        Long userId = getUserId(authentication);
        OrderResponse order = orderService.getOrderById(id, userId);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    /**
     * Cancel order
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @PathVariable Long id,
            Authentication authentication) {
        Long userId = getUserId(authentication);
        OrderResponse order = orderService.cancelOrder(id, userId);
        return ResponseEntity.ok(ApiResponse.success(order, "Order cancelled"));
    }

    /**
     * Helper method to get user ID from authentication
     */
    private Long getUserId(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }
}
