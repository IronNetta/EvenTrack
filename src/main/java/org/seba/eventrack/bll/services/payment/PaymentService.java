package org.seba.eventrack.bll.services.payment;

import com.paypal.base.rest.APIContext;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {

    String createPayment(Double amount, String currency, Long userId, Long eventId);

    boolean validatePayment(String paymentId);

    boolean refundPayment(String paymentId);

    APIContext getAPIContext();
}