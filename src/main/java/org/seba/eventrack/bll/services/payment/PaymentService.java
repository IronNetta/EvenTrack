package org.seba.eventrack.bll.services.payment;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {

    String createPayment(Double amount, String currency, Long userId, Long eventId);

    ///boolean validatePayment(String paymentId);

    boolean refundPayment(String paymentId);

    PaymentIntent getPaymentDetails(String paymentId) throws StripeException;
}