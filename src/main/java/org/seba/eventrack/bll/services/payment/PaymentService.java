package org.seba.eventrack.bll.services.payment;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {

    String createPayment(Double amount, String currency, Long userId, Long eventId, String name);

    ///boolean validatePayment(String paymentId);

    boolean refundPayment(String paymentId);

    PaymentIntent getPaymentDetails(String paymentId) throws StripeException;
}