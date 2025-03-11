package org.seba.eventrack.api.models.ticket.dtos;

import org.seba.eventrack.dl.entities.Ticket;

public record TicketDto(
    Long id,
    Long eventId,
    Long userId
) {

    public static TicketDto fromTicket(Ticket ticket) {
        return new TicketDto(ticket.getId(), ticket.getEvent().getId(), ticket.getParticipant().getId());
    }

    public long getId() {
        return id;
    }
}
