package org.seba.eventrack.api.models.event.dtos;

import org.seba.eventrack.api.models.event.forms.EventForm;
import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.enums.EventStatus;
import org.seba.eventrack.dl.enums.EventType;

import java.time.LocalDateTime;

public record EventDto(
        Long id,
        String title,
        String description,
        LocalDateTime date,
        String location,
        int capacity,
        Double price,
        EventType eventType,
        EventStatus eventStatus
) {
    public static EventDto fromEvent(Event event) {
        return new EventDto(event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getLocation(),
                event.getCapacity(),
                event.getPrice(),
                event.getEventType(),
                event.getEventStatus()
        );
    }


}
