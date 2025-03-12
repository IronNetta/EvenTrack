package org.seba.eventrack.bll.services.payment.impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.seba.eventrack.bll.services.payment.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Primary
@Service("stripe")
@RequiredArgsConstructor
public class StripePaymentServiceImpl implements PaymentService {

    @Value("${stripe.secretKey}")
    private String secretKey;
    @Value("${stripe.default-payment-method}")
    private String defaultPaymentMethod;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    @Override
    public String createPayment(Double amount, String currency, Long userId, Long eventId, String ticketType) {
        try {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("userId", userId.toString());
            metadata.put("eventId", eventId.toString());
            metadata.put("ticketType", ticketType);

            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("https://ton-site.com/success")
                    .setCancelUrl("https://ton-site.com/cancel")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)  // Toujours 1 ticket
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency(currency)
                                                    .setUnitAmount((long) (amount * 100)) // Prix en centimes
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Billet d'événement")
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);
            return session.getUrl(); // Retourne l'URL Stripe pour ce ticket
        } catch (StripeException e) {
            throw new RuntimeException("Erreur Stripe : " + e.getMessage());
        }
    }

    /*@Override
    public String createPayment(Double amount, String currency, Long userId, Long eventId) {
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) (amount * 100))
                    .setCurrency(currency)
                    .setPaymentMethod(defaultPaymentMethod)
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
    }*/

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

    public PaymentIntent getPaymentDetails(String paymentId) throws StripeException {
        return PaymentIntent.retrieve(paymentId);
    }

}
