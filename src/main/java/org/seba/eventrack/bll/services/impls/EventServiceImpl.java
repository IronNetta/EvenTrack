package org.seba.eventrack.bll.services.impls;


import lombok.RequiredArgsConstructor;
import org.seba.eventrack.bll.services.EventService;
import org.seba.eventrack.dal.repositories.TicketRepository;
import org.seba.eventrack.dal.repositories.UserRepository;
import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dal.repositories.EventRepository;
import org.seba.eventrack.dl.enums.EventStatus;
import org.seba.eventrack.il.requests.SearchParam;
import org.seba.eventrack.il.specification.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

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
    public Event update(Event event, Long id) {
        Optional<Event> existingEvent = eventRepository.findById(id);
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
        updatedEvent.setPrice(event.getPrice());
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
    public Event validateEvent(Event event) {
        if (!eventRepository.existsById(event.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        Optional<Event> existingEvent = eventRepository.findById(event.getId());
        if (existingEvent.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        Event updatedEvent = existingEvent.get();
        updatedEvent.setEventStatus(EventStatus.ACCEPTED);
        eventRepository.save(updatedEvent);
        return updatedEvent;
    }

    @Override
    public Event refuseEvent(Event event) {
        if (!eventRepository.existsById(event.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        Optional<Event> existingEvent = eventRepository.findById(event.getId());
        if (existingEvent.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        Event updatedEvent = existingEvent.get();
        event.setEventStatus(EventStatus.REJECTED);
        eventRepository.save(updatedEvent);
        return updatedEvent;
    }

    @Override
    public Page<Event> findAllByDate(int year, int month, Pageable pageable) {
        return eventRepository.findByDate(year, month, pageable);
    }

    @Override
    public Event planifyEvent(Event event, LocalDateTime date) {
        Optional<Event> existingEvent = eventRepository.findById(event.getId());
        if (existingEvent.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        Event updatedEvent = existingEvent.get();
        updatedEvent.setDate(date);
        eventRepository.save(updatedEvent);
        return updatedEvent;
    }

    @Override
    public Double GetPourcentage(Long eventId) {
        Double pourcentage = eventRepository.getRatioOfReservedSeats(eventId);
        if (!eventRepository.existsById(eventId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        } else if (pourcentage == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tickets reserved for this event");
        }
        return pourcentage;
    }

    public Double getRatioOfReservedSeats(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        return eventRepository.getRatioOfReservedSeats(eventId);
    }

    public Double getPopularity(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        if (event.getReservedSeats() < event.getCapacity()) {
            return 0.0; // Popularité = 0 si l'événement n'est pas encore complet
        }

        LocalDateTime eventCreatedAt = eventRepository.getCreationDate(eventId);
        LocalDateTime lastTicketCreatedAt = ticketRepository.getCreationDate(eventId);

        if (eventCreatedAt == null || lastTicketCreatedAt == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not retrieve timestamps");
        }

        long daysToFill = Duration.between(eventCreatedAt, lastTicketCreatedAt).toDays();

        return 1.0 / (daysToFill + 1);
    }
}
