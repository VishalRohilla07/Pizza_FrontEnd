package com.pizza.ordering.service;

import com.pizza.ordering.dto.CartResponse;
import com.pizza.ordering.dto.PizzaResponse;
import com.pizza.ordering.entity.Cart;
import com.pizza.ordering.entity.CartItem;
import com.pizza.ordering.entity.Pizza;
import com.pizza.ordering.entity.User;
import com.pizza.ordering.exception.BadRequestException;
import com.pizza.ordering.exception.ResourceNotFoundException;
import com.pizza.ordering.repository.CartItemRepository;
import com.pizza.ordering.repository.CartRepository;
import com.pizza.ordering.repository.PizzaRepository;
import com.pizza.ordering.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;

/**
 * Service for cart operations
 */
@Service
public class CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get or create cart for user
     */
    @Transactional
    public CartResponse getCart(Long userId) {
        logger.debug("Fetching cart for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        return buildCartResponse(cart);
    }

    /**
     * Add pizza to cart
     */
    @Transactional
    public CartResponse addToCart(Long userId, Long pizzaId) {
        logger.info("Adding pizza {} to cart for user {}", pizzaId, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Pizza pizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pizza", "id", pizzaId));

        // Check if pizza is available
        if (!pizza.getAvailable()) {
            throw new BadRequestException("Pizza is currently unavailable");
        }

        // Get or create cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        // Check if item already exists in cart
        CartItem existingItem = cartItemRepository.findByCartIdAndPizzaId(cart.getId(), pizzaId)
                .orElse(null);

        if (existingItem != null) {
            // Increment quantity
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            cartItemRepository.save(existingItem);
            logger.info("Incremented quantity for pizza {} in cart", pizzaId);
        } else {
            // Add new item
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setPizza(pizza);
            newItem.setQuantity(1);
            cartItemRepository.save(newItem);
            logger.info("Added new pizza {} to cart", pizzaId);
        }

        // Refresh cart
        cart = cartRepository.findById(cart.getId()).get();
        return buildCartResponse(cart);
    }

    /**
     * Update cart item quantity
     */
    @Transactional
    public CartResponse updateCartItem(Long userId, Long pizzaId, Integer quantity) {
        logger.info("Updating cart item for user {}, pizza {}, quantity {}", userId, pizzaId, quantity);

        if (quantity < 1) {
            throw new BadRequestException("Quantity must be at least 1");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));

        CartItem cartItem = cartItemRepository.findByCartIdAndPizzaId(cart.getId(), pizzaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        // Refresh cart
        cart = cartRepository.findById(cart.getId()).get();
        return buildCartResponse(cart);
    }

    /**
     * Remove item from cart
     */
    @Transactional
    public CartResponse removeFromCart(Long userId, Long pizzaId) {
        logger.info("Removing pizza {} from cart for user {}", pizzaId, userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));

        CartItem cartItem = cartItemRepository.findByCartIdAndPizzaId(cart.getId(), pizzaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        cartItemRepository.delete(cartItem);

        // Refresh cart
        cart = cartRepository.findById(cart.getId()).get();
        return buildCartResponse(cart);
    }

    /**
     * Clear cart
     */
    @Transactional
    public void clearCart(Long userId) {
        logger.info("Clearing cart for user {}", userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));

        cartItemRepository.deleteByCartId(cart.getId());
    }

    /**
     * Build cart response DTO
     */
    private CartResponse buildCartResponse(Cart cart) {
        var items = cart.getCartItems().stream()
                .map(item -> {
                    BigDecimal subtotal = item.getPizza().getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));
                    return new CartResponse.CartItemResponse(
                            item.getId(),
                            PizzaResponse.fromEntity(item.getPizza()),
                            item.getQuantity(),
                            subtotal);
                })
                .collect(Collectors.toList());

        int totalItems = cart.getCartItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(item -> item.getPizza().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(cart.getId(), items, totalItems, totalPrice);
    }
}
