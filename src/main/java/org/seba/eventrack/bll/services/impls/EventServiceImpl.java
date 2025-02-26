package org.seba.eventrack.bll.services.impls;

import lombok.RequiredArgsConstructor;
import org.seba.eventrack.bll.services.EventService;
import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dal.repositories.EventRepository;
import org.seba.eventrack.il.requests.SearchParam;
import org.seba.eventrack.il.specification.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public Page<Event> findAll(List<SearchParam<Event>> searchParams, Pageable pageable) {
        if (searchParams.isEmpty()) {
            return eventRepository.findAll(pageable);
        }
        return eventRepository.findAll(
                Specification.allOf(
                        searchParams.stream()
                                .map(SearchSpecification::search)
                                .toList()
                ),
                pageable
        );
    }

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    @Override
    public Event update(Event event) {
        Optional<Event> existingEvent = eventRepository.findById(event.getId());
        if (existingEvent.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        Event updatedEvent = existingEvent.get();
        updatedEvent.setTitle(event.getTitle());
        updatedEvent.setDescription(event.getDescription());
        updatedEvent.setLocation(event.getLocation());
        updatedEvent.setCapacity(event.getCapacity());
        updatedEvent.setReservedSeats(event.getReservedSeats());
        updatedEvent.setImageUrl(event.getImageUrl());
        updatedEvent.setEventType(event.getEventType());
        return eventRepository.save(updatedEvent);
    }

    @Override
    public void deleteById(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        eventRepository.deleteById(id);
    }
}
