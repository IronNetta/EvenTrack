package org.seba.eventrack.api.controllers;

import lombok.RequiredArgsConstructor;
import org.seba.eventrack.bll.services.TicketService;
import org.seba.eventrack.dl.entities.Ticket;
import org.seba.eventrack.il.requests.SearchParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER') or hasRole('PARTICIPANT')")
    @GetMapping
    public ResponseEntity<Page<Ticket>> getAllTickets(
            @RequestParam Map<String, String> params,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        List<SearchParam<Ticket>> searchParams = SearchParam.create(params);
        Page<Ticket> tickets = ticketService.findAll(searchParams, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort)));
        return ResponseEntity.ok(tickets);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PARTICIPANT')")
    @PostMapping("/book")
    public ResponseEntity<String> bookTicket(@RequestParam Long eventId, @RequestParam Long userId) {
        return ResponseEntity.ok(ticketService.bookTicket(eventId, userId));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PARTICIPANT')")
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PARTICIPANT')")
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelTicket(@PathVariable Long id) {
        ticketService.cancelTicket(id);
        return ResponseEntity.noContent().build();
    }
}
