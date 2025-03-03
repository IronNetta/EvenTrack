package org.seba.eventrack.bll.services.impls;

import lombok.RequiredArgsConstructor;
import org.seba.eventrack.api.models.event.dtos.EventDto;
import org.seba.eventrack.bll.services.EventService;
import org.seba.eventrack.dal.repositories.UserRepository;
import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dal.repositories.EventRepository;
import org.seba.eventrack.dl.entities.User;
import org.seba.eventrack.dl.enums.EventStatus;
import org.seba.eventrack.dl.enums.UserRole;
import org.seba.eventrack.il.requests.SearchParam;
import org.seba.eventrack.il.specification.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

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
        updatedEvent.setEventStatus(event.getEventStatus());
        return eventRepository.save(updatedEvent);
    }

    @Override
    public void deleteById(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        eventRepository.deleteById(id);
    }

    @Override
    public Event validateEvent(Event event, User user) {
        if (!eventRepository.existsById(event.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        if (!userRepository.existsById(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (!user.getRole().equals(UserRole.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this resource");
        }
        event.setEventStatus(EventStatus.ACCEPTED);
        update(event);
        return event;
    }

    @Override
    public Event refuseEvent(Event event, User user) {
        if (!eventRepository.existsById(event.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        if (!userRepository.existsById(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (!user.getRole().equals(UserRole.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this resource");
        }
        event.setEventStatus(EventStatus.REJECTED);
        update(event);
        return event;
    }
}
