package org.seba.eventrack.api.models.event.forms;

import org.seba.eventrack.dl.entities.Event;

public record EventForm(
        String title,
        String description,
        String location,
        int capacity
) {
    public Event toEvent(EventForm eventForm) {
        return new Event(title, description, location, capacity);
    }
}
