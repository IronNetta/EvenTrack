package org.seba.eventrack.api.models.event.forms;

import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.entities.User;
import org.seba.eventrack.dl.enums.EventType;
import org.seba.eventrack.dl.enums.TicketType;

import java.util.Map;

public record EventForm(
        String title,
        String description,
        String location,
        int capacity,
        String imageUrl,
        Double price,
        Map<TicketType, Double> ticketPrices,
        EventType eventType
) {
    public Event toEvent() {
        Event event = new Event(title, description, location, capacity, imageUrl, ticketPrices.get(TicketType.STANDARD), eventType);

        ticketPrices.forEach(event::setTicketPrice);
        return event;
    }
}
