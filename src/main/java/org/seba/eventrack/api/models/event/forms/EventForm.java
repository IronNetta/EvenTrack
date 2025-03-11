package org.seba.eventrack.api.models.event.forms;

import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.entities.User;
import org.seba.eventrack.dl.enums.EventType;

public record EventForm(
        String title,
        String description,
        String location,
        int capacity,
        String imageUrl,
        Double price,
        EventType eventType
) {
    public Event toEvent() {
        return new Event(title, description, location, capacity, imageUrl, price, eventType);
    }
}
