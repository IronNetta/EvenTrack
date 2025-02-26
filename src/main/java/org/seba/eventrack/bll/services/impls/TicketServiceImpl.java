package org.seba.eventrack.bll.services.impls;

import lombok.RequiredArgsConstructor;
import org.seba.eventrack.bll.services.TicketService;
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
    public Ticket bookTicket(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (event.getReservedSeats() >= event.getCapacity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No tickets available for this event");
        }

        Ticket ticket = new Ticket();
        ticket.setPaid(false);
        ticket.setEvent(event);
        ticket.setParticipant(user);
        ticket.setQrCodeUrl("GENERATED_QR_CODE");
        event.setReservedSeats(event.getReservedSeats() + 1);
        eventRepository.save(event);
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
    public Ticket cancelTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));

        Event event = ticket.getEvent();
        event.setReservedSeats(event.getReservedSeats() - 1);
        eventRepository.save(event);
        ticketRepository.delete(ticket);
        return ticket;
    }
}
