package org.seba.eventrack.api.models.ticket.dtos;

import org.seba.eventrack.dl.entities.Ticket;
import org.seba.eventrack.dl.enums.TicketType;

public record TicketDto(
    Long id,
    Long eventId,
    Long userId,
    TicketType type
) {

    public TicketDto(Long id, Long eventId, Long userId, TicketType type) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.type = type;
    }

    public static TicketDto fromTicket(Ticket ticket) {
        return new TicketDto(ticket.getId(), ticket.getEvent().getId(), ticket.getParticipant().getId(), ticket.getType());
    }

    public long getId() {
        return id;
    }
}
