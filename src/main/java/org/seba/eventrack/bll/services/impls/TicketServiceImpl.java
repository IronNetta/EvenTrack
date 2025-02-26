package org.seba.eventrack.bll.services.impls;

import lombok.RequiredArgsConstructor;
import org.seba.eventrack.bll.services.TicketService;
import org.seba.eventrack.dl.entities.Ticket;
import org.seba.eventrack.dal.repositories.TicketRepository;
import org.seba.eventrack.il.requests.SearchParam;
import org.seba.eventrack.il.specification.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

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
    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket findById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));
    }

    @Override
    public Ticket findByQrCodeUrl(String qrCodeUrl) {
        return ticketRepository.findByQrCodeUrl(qrCodeUrl)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));
    }

    @Override
    public Ticket update(Ticket ticket) {
        Optional<Ticket> existingTicket = ticketRepository.findById(ticket.getId());
        if (existingTicket.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found");
        }
        Ticket updatedTicket = existingTicket.get();
        updatedTicket.setQrCodeUrl(ticket.getQrCodeUrl());
        updatedTicket.setPaid(ticket.isPaid());
        updatedTicket.setEvent(ticket.getEvent());
        updatedTicket.setParticipant(ticket.getParticipant());
        return ticketRepository.save(updatedTicket);
    }

    @Override
    public void deleteById(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found");
        }
        ticketRepository.deleteById(id);
    }
}
