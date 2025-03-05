package org.seba.eventrack.api.controllers.payment;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.seba.eventrack.bll.services.TicketService;
import org.seba.eventrack.dl.entities.Ticket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    /*private final Map<String, PaymentService> paymentServices;
    private final TicketService ticketService;

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

    @GetMapping("/{provider}/validate/{paymentId}")
    public ResponseEntity<Boolean> validatePayment(
            @PathVariable String provider,
            @PathVariable String paymentId) throws PayPalRESTException {

        PaymentService paymentService = getPaymentService(provider);
        boolean isValid = paymentService.validatePayment(paymentId);
        // Todo ticketService.confirmTicket(paymentId);
        return ResponseEntity.ok(isValid);
    }

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
    }*/

    private final TicketService ticketService;

    @Value("${stripe.secretKey}")
    private String STRIPE_SECRET_KEY;
    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;
    private static final String SUCCESS_URL = "https://ton-site.com/success";
    private static final String CANCEL_URL = "https://ton-site.com/cancel";

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession() {
        Stripe.apiKey = STRIPE_SECRET_KEY;

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(SUCCESS_URL)
                    .setCancelUrl(CANCEL_URL)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("eur")
                                                    .setUnitAmount(2000L) // 20.00€ en centimes
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Billet de concert")
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);

            Map<String, String> response = new HashMap<>();
            response.put("url", session.getUrl());

            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
                                                      @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            // Vérification de la signature Stripe
            Event event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);

            // Vérifie le type de l'événement
            if ("checkout.session.completed".equals(((com.stripe.model.Event) event).getType())) {
                // Récupère les données de l'événement
                Session session = (Session) event.getData().getObject();
                String paymentId = session.getPaymentIntent(); // ID du paiement Stripe

                // Confirme le ticket après paiement validé
                Ticket ticket = ticketService.confirmTicket(paymentId);
                return ResponseEntity.ok("Ticket confirmé: " + ticket.getId());
            }
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signature invalide");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur");
        }

        return ResponseEntity.ok("Événement ignoré");
    }

}

