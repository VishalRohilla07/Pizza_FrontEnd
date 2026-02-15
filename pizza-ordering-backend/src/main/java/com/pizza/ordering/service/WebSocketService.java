package com.pizza.ordering.service;

import com.pizza.ordering.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for sending WebSocket messages
 */
@Service
public class WebSocketService {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Send order status update to specific user
     */
    public void sendOrderUpdate(Long orderId, Long userId, Order.OrderStatus status) {
        logger.info("Sending order update via WebSocket: order={}, status={}", orderId, status);

        Map<String, Object> message = new HashMap<>();
        message.put("orderId", orderId);
        message.put("status", status.name());
        message.put("timestamp", System.currentTimeMillis());

        // Send to specific user's topic
        messagingTemplate.convertAndSend("/topic/orders/" + userId, message);

        logger.debug("Order update sent to user {}", userId);
    }

    /**
     * Broadcast order update to all admins
     */
    public void broadcastOrderUpdate(Long orderId, Order.OrderStatus status) {
        logger.info("Broadcasting order update: order={}, status={}", orderId, status);

        Map<String, Object> message = new HashMap<>();
        message.put("orderId", orderId);
        message.put("status", status.name());
        message.put("timestamp", System.currentTimeMillis());

        // Broadcast to admin topic
        messagingTemplate.convertAndSend("/topic/admin/orders", message);
    }
}
