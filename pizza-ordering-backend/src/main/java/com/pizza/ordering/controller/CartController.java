package com.pizza.ordering.controller;

import com.pizza.ordering.dto.ApiResponse;
import com.pizza.ordering.dto.CartItemRequest;
import com.pizza.ordering.dto.CartResponse;
import com.pizza.ordering.entity.User;
import com.pizza.ordering.repository.UserRepository;
import com.pizza.ordering.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for cart operations
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get current user's cart
     */
    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart(Authentication authentication) {
        Long userId = getUserId(authentication);
        CartResponse cart = cartService.getCart(userId);
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    /**
     * Add item to cart
     */
    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(
            @Valid @RequestBody CartItemRequest request,
            Authentication authentication) {
        Long userId = getUserId(authentication);
        CartResponse cart = cartService.addToCart(userId, request.getPizzaId());
        return ResponseEntity.ok(ApiResponse.success(cart, "Item added to cart"));
    }

    /**
     * Update cart item quantity
     */
    @PutMapping("/items/{pizzaId}")
    public ResponseEntity<ApiResponse<CartResponse>> updateCartItem(
            @PathVariable Long pizzaId,
            @RequestParam Integer quantity,
            Authentication authentication) {
        Long userId = getUserId(authentication);
        CartResponse cart = cartService.updateCartItem(userId, pizzaId, quantity);
        return ResponseEntity.ok(ApiResponse.success(cart, "Cart updated"));
    }

    /**
     * Remove item from cart
     */
    @DeleteMapping("/items/{pizzaId}")
    public ResponseEntity<ApiResponse<CartResponse>> removeFromCart(
            @PathVariable Long pizzaId,
            Authentication authentication) {
        Long userId = getUserId(authentication);
        CartResponse cart = cartService.removeFromCart(userId, pizzaId);
        return ResponseEntity.ok(ApiResponse.success(cart, "Item removed from cart"));
    }

    /**
     * Clear cart
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearCart(Authentication authentication) {
        Long userId = getUserId(authentication);
        cartService.clearCart(userId);
        return ResponseEntity.ok(ApiResponse.success(null, "Cart cleared"));
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
