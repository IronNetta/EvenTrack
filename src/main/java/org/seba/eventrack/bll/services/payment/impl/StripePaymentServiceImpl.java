package org.seba.eventrack.bll.services.payment.impl;

import com.paypal.base.rest.APIContext;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.seba.eventrack.bll.services.payment.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("stripe")
@RequiredArgsConstructor
public class StripePaymentServiceImpl implements PaymentService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    @Override
    public String createPayment(Double amount, String currency, Long userId, Long eventId) {
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) (amount * 100))
                    .setCurrency(currency)
                    .putMetadata("userId", userId.toString())
                    .putMetadata("eventId", eventId.toString())
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);
            return intent.getClientSecret();
        } catch (StripeException e) {
            throw new RuntimeException("Failed to create Stripe payment", e);
        }
    }

    @Override
    public boolean validatePayment(String paymentId) {
        try {
            PaymentIntent intent = PaymentIntent.retrieve(paymentId);
            return "succeeded".equals(intent.getStatus());
        } catch (StripeException e) {
            throw new RuntimeException("Failed to validate Stripe payment", e);
        }
    }

    @Override
    public boolean refundPayment(String paymentId) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("payment_intent", paymentId);
            Refund.create(params);
            return true;
        } catch (StripeException e) {
            throw new RuntimeException("Failed to refund Stripe payment", e);
        }
    }

    @Override
    public APIContext getAPIContext() {
        return null;
    }
}
