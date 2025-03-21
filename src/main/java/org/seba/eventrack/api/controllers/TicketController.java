package org.seba.eventrack.api.controllers;

import com.paypal.base.rest.PayPalRESTException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.seba.eventrack.api.models.CustomPage;
import org.seba.eventrack.api.models.ticket.dtos.TicketDto;
import org.seba.eventrack.bll.services.TicketService;
import org.seba.eventrack.bll.services.payment.PaymentService;
import org.seba.eventrack.dl.entities.Ticket;
import org.seba.eventrack.dl.enums.TicketType;
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
@Tag(name = "Tickets", description = "Endpoints pour la gestion des tickets")
public class TicketController {

    private final TicketService ticketService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<CustomPage<TicketDto>> getAllTickets(
            @RequestParam Map<String, String> params,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        List<SearchParam<Ticket>> searchParams = SearchParam.create(params);
        Page<Ticket> tickets = ticketService.findAll(searchParams,
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, sort)));
        List<TicketDto> dtos = tickets.getContent().stream()
                .map(TicketDto::fromTicket)
                .toList();
        CustomPage<TicketDto> result = new CustomPage<>(dtos, tickets.getTotalPages(), tickets.getNumber() + 1);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/book")
    public ResponseEntity<String> bookTicket(@RequestParam Long eventId,
                                             @RequestParam Long userId,
                                             @RequestParam TicketType ticketType) {
        String paymentUrl = ticketService.bookTicket(eventId, userId, ticketType);
        return ResponseEntity.ok(paymentUrl); // Retourne l'URL Stripe immédiatement
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.findById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelTicket(@PathVariable Long id) {
        ticketService.cancelTicket(id);
        return ResponseEntity.noContent().build();
    }
}
