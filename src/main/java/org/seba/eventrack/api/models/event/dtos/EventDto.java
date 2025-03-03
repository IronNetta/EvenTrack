package org.seba.eventrack.api.models.event.dtos;

import org.seba.eventrack.dl.entities.Event;

public record EventDto(
    Long id,
    String name,
    String description
) {
    public static EventDto fromEvent(Event event) {
        return new EventDto(event.getId(), event.getTitle(), event.getDescription());
    }
}
