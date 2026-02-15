package com.pizza.ordering.service;

import com.pizza.ordering.dto.PaymentIntentResponse;
import com.pizza.ordering.entity.Order;
import com.pizza.ordering.exception.PaymentFailedException;
import com.pizza.ordering.exception.ResourceNotFoundException;
import com.pizza.ordering.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;

/**
 * Service for Stripe payment integration
 */
@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    /**
     * Create Stripe payment intent for order
     */
    @Transactional
    public PaymentIntentResponse createPaymentIntent(Long orderId) {
        logger.info("Creating payment intent for order {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        // Check if payment intent already exists
        if (order.getPaymentIntentId() != null) {
            throw new PaymentFailedException("Payment intent already exists for this order");
        }

        try {
            // Convert amount to cents (Stripe uses smallest currency unit)
            long amountInCents = order.getTotalAmount()
                    .multiply(BigDecimal.valueOf(100))
                    .longValue();

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency("usd")
                    .putMetadata("orderId", order.getId().toString())
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build())
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Save payment intent ID to order
            order.setPaymentIntentId(paymentIntent.getId());
            orderRepository.save(order);

            logger.info("Payment intent created: {}", paymentIntent.getId());

            return new PaymentIntentResponse(
                    paymentIntent.getClientSecret(),
                    paymentIntent.getId(),
                    order.getId());

        } catch (StripeException e) {
            logger.error("Failed to create payment intent", e);
            throw new PaymentFailedException("Failed to create payment intent: " + e.getMessage(), e);
        }
    }

    /**
     * Handle payment success
     */
    @Transactional
    public void handlePaymentSuccess(String paymentIntentId) {
        logger.info("Handling payment success for intent: {}", paymentIntentId);

        Order order = orderService.getOrderByPaymentIntentId(paymentIntentId);
        orderService.updatePaymentStatus(order.getId(), Order.PaymentStatus.SUCCESS);

        // Auto-confirm order after successful payment
        if (order.getOrderStatus() == Order.OrderStatus.PLACED) {
            orderService.updateOrderStatus(order.getId(), Order.OrderStatus.CONFIRMED);
        }

        logger.info("Payment success processed for order {}", order.getId());
    }

    /**
     * Handle payment failure
     */
    @Transactional
    public void handlePaymentFailure(String paymentIntentId) {
        logger.info("Handling payment failure for intent: {}", paymentIntentId);

        Order order = orderService.getOrderByPaymentIntentId(paymentIntentId);
        orderService.updatePaymentStatus(order.getId(), Order.PaymentStatus.FAILED);

        logger.info("Payment failure processed for order {}", order.getId());
    }

    /**
     * Handle Stripe webhook events
     */
    public void handleWebhook(String payload, String sigHeader) {
        logger.info("Processing Stripe webhook");

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (Exception e) {
            logger.error("Webhook signature verification failed", e);
            throw new PaymentFailedException("Invalid webhook signature");
        }

        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject().orElse(null);
                if (paymentIntent != null) {
                    handlePaymentSuccess(paymentIntent.getId());
                }
                break;

            case "payment_intent.payment_failed":
                PaymentIntent failedIntent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject().orElse(null);
                if (failedIntent != null) {
                    handlePaymentFailure(failedIntent.getId());
                }
                break;

            default:
                logger.info("Unhandled event type: {}", event.getType());
        }
    }
}
