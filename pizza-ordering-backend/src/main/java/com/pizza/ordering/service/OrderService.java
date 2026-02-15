package com.pizza.ordering.service;

import com.pizza.ordering.dto.OrderResponse;
import com.pizza.ordering.dto.PizzaResponse;
import com.pizza.ordering.entity.*;
import com.pizza.ordering.exception.BadRequestException;
import com.pizza.ordering.exception.ResourceNotFoundException;
import com.pizza.ordering.exception.UnauthorizedException;
import com.pizza.ordering.repository.CartRepository;
import com.pizza.ordering.repository.OrderRepository;
import com.pizza.ordering.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for order operations
 */
@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    /**
     * Create order from cart
     */
    @Transactional
    public OrderResponse createOrder(Long userId) {
        logger.info("Creating order for user {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException("Cart is empty"));

        if (cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cannot place order with empty cart");
        }

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(Order.OrderStatus.PLACED);
        order.setPaymentStatus(Order.PaymentStatus.PENDING);

        // Calculate total and create order items
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setPizza(cartItem.getPizza());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPizza().getPrice()); // Snapshot price

            order.addItem(orderItem);

            BigDecimal itemTotal = cartItem.getPizza().getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);

        // Clear cart after order creation
        cartService.clearCart(userId);

        logger.info("Order created with ID: {}", order.getId());
        return buildOrderResponse(order);
    }

    /**
     * Get order by ID
     */
    public OrderResponse getOrderById(Long orderId, Long userId) {
        logger.debug("Fetching order {} for user {}", orderId, userId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        // Verify ownership
        if (!order.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You don't have permission to view this order");
        }

        return buildOrderResponse(order);
    }

    /**
     * Get all orders for a user
     */
    public List<OrderResponse> getUserOrders(Long userId) {
        logger.debug("Fetching orders for user {}", userId);

        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return orders.stream()
                .map(this::buildOrderResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all orders (Admin only)
     */
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        logger.debug("Fetching all orders with pagination");

        Page<Order> orders = orderRepository.findAllByOrderByCreatedAtDesc(pageable);
        return orders.map(this::buildOrderResponse);
    }

    /**
     * Get orders by status (Admin only)
     */
    public Page<OrderResponse> getOrdersByStatus(Order.OrderStatus status, Pageable pageable) {
        logger.debug("Fetching orders with status: {}", status);

        Page<Order> orders = orderRepository.findByOrderStatus(status, pageable);
        return orders.map(this::buildOrderResponse);
    }

    /**
     * Update order status (Admin only)
     */
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
        logger.info("Updating order {} status to {}", orderId, newStatus);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        // Validate status transition
        if (!order.canTransitionTo(newStatus)) {
            throw new BadRequestException(
                    String.format("Cannot transition from %s to %s", order.getOrderStatus(), newStatus));
        }

        order.setOrderStatus(newStatus);
        order = orderRepository.save(order);

        logger.info("Order {} status updated to {}", orderId, newStatus);
        return buildOrderResponse(order);
    }

    /**
     * Cancel order (Customer)
     */
    @Transactional
    public OrderResponse cancelOrder(Long orderId, Long userId) {
        logger.info("Cancelling order {} for user {}", orderId, userId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        // Verify ownership
        if (!order.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You don't have permission to cancel this order");
        }

        // Check if order can be cancelled
        if (!order.canBeCancelled()) {
            throw new BadRequestException("Order cannot be cancelled in current status: " + order.getOrderStatus());
        }

        order.setOrderStatus(Order.OrderStatus.CANCELLED);
        order = orderRepository.save(order);

        logger.info("Order {} cancelled", orderId);
        return buildOrderResponse(order);
    }

    /**
     * Update payment status
     */
    @Transactional
    public void updatePaymentStatus(Long orderId, Order.PaymentStatus paymentStatus) {
        logger.info("Updating payment status for order {} to {}", orderId, paymentStatus);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        order.setPaymentStatus(paymentStatus);
        orderRepository.save(order);
    }

    /**
     * Get order by payment intent ID
     */
    public Order getOrderByPaymentIntentId(String paymentIntentId) {
        return orderRepository.findByPaymentIntentId(paymentIntentId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for payment intent"));
    }

    /**
     * Build order response DTO
     */
    private OrderResponse buildOrderResponse(Order order) {
        var items = order.getOrderItems().stream()
                .map(item -> {
                    BigDecimal subtotal = item.getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));
                    return new OrderResponse.OrderItemResponse(
                            item.getId(),
                            PizzaResponse.fromEntity(item.getPizza()),
                            item.getQuantity(),
                            item.getPrice(),
                            subtotal);
                })
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                items,
                order.getTotalAmount(),
                order.getOrderStatus(),
                order.getPaymentStatus(),
                order.getPaymentIntentId(),
                order.getCreatedAt(),
                order.getUpdatedAt());
    }
}
