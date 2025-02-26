/*package org.seba.eventrack.bll.services.payment.impl;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import lombok.RequiredArgsConstructor;
import org.seba.eventrack.bll.services.payment.PaymentService;
import org.seba.eventrack.dl.entities.Ticket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("paypal")
@RequiredArgsConstructor
public class PayPalPaymentServiceImpl implements PaymentService {

    @Value("${paypal.clientId}")
    private String clientId;

    @Value("${paypal.clientSecret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    public APIContext getAPIContext() {
        return new APIContext(clientId, clientSecret, mode);
    }

    @Override
    public String createPayment(Double amount, String currency, Long userId, Long eventId) {
        Amount paymentAmount = new Amount();
        paymentAmount.setCurrency(currency);
        paymentAmount.setTotal(String.format("%.2f", amount));

        Transaction transaction = new Transaction();
        transaction.setAmount(paymentAmount);
        transaction.setDescription("Event ID: " + eventId + ", User ID: " + userId);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://yourapp.com/cancel");
        redirectUrls.setReturnUrl("http://yourapp.com/success");
        payment.setRedirectUrls(redirectUrls);

        try {
            APIContext apiContext = getAPIContext();
            Payment createdPayment = payment.create(apiContext);
            return createdPayment.getLinks().stream()
                    .filter(link -> "approval_url".equals(link.getRel()))
                    .findFirst()
                    .map(Links::getHref)
                    .orElseThrow(() -> new RuntimeException("Approval URL not found"));
        } catch (PayPalRESTException e) {
            throw new RuntimeException("Failed to create PayPal payment", e);
        }
    }

    @Override
    public boolean validatePayment(String paymentId) {
        try {
            APIContext apiContext = getAPIContext();
            Payment payment = Payment.get(apiContext, paymentId);
            return "approved".equalsIgnoreCase(payment.getState());
        } catch (PayPalRESTException e) {
            throw new RuntimeException("Failed to validate PayPal payment", e);
        }
    }

    @Override
    public boolean refundPayment(String paymentId) {
        try {
            APIContext apiContext = getAPIContext();
            Sale sale = Sale.get(apiContext, paymentId);

            RefundRequest refundRequest = new RefundRequest();
            Amount refundAmount = new Amount();
            refundAmount.setCurrency(sale.getAmount().getCurrency());
            refundAmount.setTotal(sale.getAmount().getTotal());

            refundRequest.setAmount(refundAmount);

            sale.refund(apiContext, refundRequest);
            return true;
        } catch (PayPalRESTException e) {
            throw new RuntimeException("Failed to refund PayPal payment", e);
        }
    }
}
*/
