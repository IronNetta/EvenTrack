package org.seba.eventrack.bll.services;

import org.seba.eventrack.dl.entities.Ticket;
import org.seba.eventrack.il.requests.SearchParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TicketService {
    Page<Ticket> findAll(List<SearchParam<Ticket>> searchParams, Pageable pageable);
    Ticket bookTicket(Long eventId, Long userId);
    Ticket findById(Long id);
    Ticket findByQrCodeUrl(String qrCodeUrl);
    Ticket cancelTicket(Long ticketId);
}