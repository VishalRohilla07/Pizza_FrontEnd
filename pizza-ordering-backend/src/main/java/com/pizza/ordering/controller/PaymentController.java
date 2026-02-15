package com.pizza.ordering.controller;

import com.pizza.ordering.dto.ApiResponse;
import com.pizza.ordering.dto.PaymentIntentResponse;
import com.pizza.ordering.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for payment operations
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * Create payment intent for order
     */
    @PostMapping("/create-intent")
    public ResponseEntity<ApiResponse<PaymentIntentResponse>> createPaymentIntent(@RequestParam Long orderId) {
        PaymentIntentResponse response = paymentService.createPaymentIntent(orderId);
        return ResponseEntity.ok(ApiResponse.success(response, "Payment intent created"));
    }

    /**
     * Handle Stripe webhook events
     */
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        paymentService.handleWebhook(payload, sigHeader);
        return ResponseEntity.ok("Webhook processed");
    }
}
