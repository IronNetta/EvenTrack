package org.seba.eventrack.api.models.event.forms;

import io.swagger.v3.oas.annotations.media.Schema;
import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.enums.EventType;
import org.seba.eventrack.dl.enums.TicketType;
import org.seba.eventrack.api.models.ticket.dtos.TicketPriceDTO;

import java.util.HashMap;
import java.util.Map;

public record EventForm(
        String title,
        String description,
        String location,
        int capacity,
        String imageUrl,
        //Double price,
        //Map<TicketType, Double> ticketPrices,
        EventType eventType,
        @Schema(description = "Ticket prices by type")
        TicketPriceDTO ticketPrices
) {
    public Event toEvent() {
        Double standardPrice = ticketPrices.STANDARD();
        Event event = new Event(title, description, location, capacity, imageUrl, standardPrice, eventType);

        Map<TicketType, Double> priceMap = new HashMap<>();
        if (ticketPrices.STANDARD() != null) priceMap.put(TicketType.STANDARD, ticketPrices.STANDARD());
        if (ticketPrices.VIP() != null) priceMap.put(TicketType.VIP, ticketPrices.VIP());
        if (ticketPrices.YOUNG() != null) priceMap.put(TicketType.YOUNG, ticketPrices.YOUNG());
        return event;
    }
}
