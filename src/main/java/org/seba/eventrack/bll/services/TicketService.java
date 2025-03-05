package org.seba.eventrack.bll.services;

import com.paypal.base.rest.PayPalRESTException;
import com.stripe.exception.StripeException;
import org.seba.eventrack.api.models.ticket.dtos.TicketDto;
import org.seba.eventrack.dl.entities.Ticket;
import org.seba.eventrack.il.requests.SearchParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TicketService {
    Page<Ticket> findAll(List<SearchParam<Ticket>> searchParams, Pageable pageable);
    String bookTicket(Long eventId, Long userId);
    Ticket confirmTicket(String paymentId) throws StripeException;
    TicketDto findById(Long id);
    Ticket findByQrCodeUrl(String qrCodeUrl);
    Ticket cancelTicket(Long ticketId);
}