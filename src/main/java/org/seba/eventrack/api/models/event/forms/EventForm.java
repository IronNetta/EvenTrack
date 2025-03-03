package org.seba.eventrack.api.models.event.forms;

import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.enums.EventStatus;

import java.time.LocalDateTime;

public record EventForm(
        Long id,
        String title,
        String description,
        LocalDateTime date,
        String location,
        int capacity,
        EventStatus eventStatus
) {
    public Event toEvent(EventForm eventForm) {
        return new Event(title, description, location, capacity);
    }

    public static EventForm fromEvent(Event event) {
        return new EventForm(event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getLocation(),
                event.getCapacity(),
                event.getEventStatus()
        );
    }
}
