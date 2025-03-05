package org.seba.eventrack.bll.services.impls;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.seba.eventrack.api.models.mails.dtos.EmailsDTO;
import org.seba.eventrack.api.models.ticket.dtos.TicketDto;
import org.seba.eventrack.bll.services.TicketService;
import org.seba.eventrack.bll.services.mails.EmailService;
import org.seba.eventrack.bll.services.payment.PaymentService;
import org.seba.eventrack.bll.services.qrCode.QRCodeService;
import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.entities.Ticket;
import org.seba.eventrack.dl.entities.User;
import org.seba.eventrack.dal.repositories.EventRepository;
import org.seba.eventrack.dal.repositories.TicketRepository;
import org.seba.eventrack.dal.repositories.UserRepository;
import org.seba.eventrack.il.requests.SearchParam;
import org.seba.eventrack.il.specification.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final QRCodeService qrCodeService;
    private final EmailService emailService;

    @Override
    public Page<Ticket> findAll(List<SearchParam<Ticket>> searchParams, Pageable pageable) {
        if (searchParams.isEmpty()) {
            return ticketRepository.findAll(pageable);
        }
        return ticketRepository.findAll(
                Specification.allOf(
                        searchParams.stream()
                                .map(SearchSpecification::search)
                                .toList()
                ),
                pageable
        );
    }

    @Override
    public String bookTicket(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (event.getReservedSeats() >= event.getCapacity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No tickets available for this event");
        }

        /*Test mail sans Stripe
        Ticket ticket = new Ticket();
        ticket.setPaid(true);
        ticket.setEvent(event);
        ticket.setParticipant(user);
        String qrCodePath = qrCodeService.generateQrCode(ticket.getId());
        ticket.setQrCodeUrl(qrCodePath);
        emailService.sendMailWithAttachment(new EmailsDTO(user.getEmail(), "Ticket Confirmation", "Ticket", qrCodePath));*/

        return paymentService.createPayment(event.getPrice(), "USD", userId, eventId);
    }

    @Override
    public Ticket confirmTicket(String paymentId) throws StripeException {
        // Vérifie le paiement Stripe
        PaymentIntent payment = paymentService.getPaymentDetails(paymentId);
        if (!"succeeded".equals(payment.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment validation failed");
        }

        // Récupérer les metadata Stripe contenant userId et eventId
        Long userId = Long.parseLong(payment.getMetadata().get("userId"));
        Long eventId = Long.parseLong(payment.getMetadata().get("eventId"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Création du ticket
        Ticket ticket = new Ticket();
        ticket.setPaid(true);
        ticket.setEvent(event);
        ticket.setParticipant(user);

        // Génération du QR Code
        String qrCodePath = qrCodeService.generateQrCode(ticket.getId());
        ticket.setQrCodeUrl(qrCodePath);

        // Mise à jour du nombre de places réservées
        event.setReservedSeats(event.getReservedSeats() + 1);
        eventRepository.save(event);

        // Envoi du mail avec le ticket
        emailService.sendSimpleMail(new EmailsDTO(user.getEmail(), "Ticket Confirmation", "Ticket"));
        emailService.sendMailWithAttachment(new EmailsDTO(user.getEmail(), "Ticket Confirmation", "Ticket", qrCodePath));

        return ticketRepository.save(ticket);
    }

    /*@Override
    public Ticket confirmTicket(String paymentId) throws PayPalRESTException {
        if (paymentService.validatePayment(paymentId)) {
            Payment payment = paymentService.getPaymentDetails(paymentId);

            String description = payment.getTransactions().get(0).getDescription();
            String[] parts = description.split(": ");
            Long userId = Long.parseLong(parts[1]);
            Long eventId = Long.parseLong(parts[3]);

            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            Ticket ticket = new Ticket();
            ticket.setPaid(true);
            ticket.setEvent(event);
            ticket.setParticipant(user);

            String qrCodePath = qrCodeService.generateQrCode(ticket.getId());
            ticket.setQrCodeUrl(qrCodePath);

            event.setReservedSeats(event.getReservedSeats() + 1);
            eventRepository.save(event);

            emailService.sendSimpleMail(new EmailsDTO(user.getEmail(), "Ticket Confirmation", "Ticket"));
            emailService.sendMailWithAttachment(new EmailsDTO(user.getEmail(), "Ticket Confirmation", "Ticket", qrCodePath));
            return ticketRepository.save(ticket);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment validation failed");
    }*/
    @Override
    public TicketDto findById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));
        return TicketDto.fromTicket(ticket);
    }

    @Override
    public Ticket findByQrCodeUrl(String qrCodeUrl) {
        return ticketRepository.findByQrCodeUrl(qrCodeUrl)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));
    }

    @Override
    public Ticket cancelTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));

        if (ticket.isPaid()) {
            boolean refunded = paymentService.refundPayment(ticket.getPaymentId());
            if (!refunded) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refund failed");
            }
        }

        Event event = ticket.getEvent();
        event.setReservedSeats(event.getReservedSeats() - 1);
        eventRepository.save(event);
        ticketRepository.delete(ticket);
        return ticket;
    }

}
