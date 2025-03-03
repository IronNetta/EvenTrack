package org.seba.eventrack.api.controllers.payment;

import lombok.RequiredArgsConstructor;
import org.seba.eventrack.bll.services.payment.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final Map<String, PaymentService> paymentServices;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{provider}/create")
    public ResponseEntity<String> createPayment(
            @PathVariable String provider,
            @RequestParam Double amount,
            @RequestParam String currency,
            @RequestParam Long userId,
            @RequestParam Long eventId) {

        PaymentService paymentService = getPaymentService(provider);
        String paymentUrlOrSecret = paymentService.createPayment(amount, currency, userId, eventId);
        return ResponseEntity.ok(paymentUrlOrSecret);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{provider}/validate/{paymentId}")
    public ResponseEntity<Boolean> validatePayment(
            @PathVariable String provider,
            @PathVariable String paymentId) {

        PaymentService paymentService = getPaymentService(provider);
        boolean isValid = paymentService.validatePayment(paymentId);
        return ResponseEntity.ok(isValid);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{provider}/refund/{paymentId}")
    public ResponseEntity<Boolean> refundPayment(
            @PathVariable String provider,
            @PathVariable String paymentId) {

        PaymentService paymentService = getPaymentService(provider);
        boolean isRefunded = paymentService.refundPayment(paymentId);
        return ResponseEntity.ok(isRefunded);
    }

    private PaymentService getPaymentService(String provider) {
        PaymentService paymentService = paymentServices.get(provider.toLowerCase());
        if (paymentService == null) {
            throw new IllegalArgumentException("Invalid payment provider: " + provider);
        }
        return paymentService;
    }
}

