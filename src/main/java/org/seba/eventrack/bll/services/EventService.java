package org.seba.eventrack.bll.services;

import org.seba.eventrack.api.models.event.dtos.EventDto;
import org.seba.eventrack.api.models.event.forms.EventForm;
import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.entities.User;
import org.seba.eventrack.il.requests.SearchParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    Page<Event> findAll(List<SearchParam<Event>> searchParams, Pageable pageable);

    Event save(Event event);

    Event findById(Long id);

    Event update(Event event);

    void deleteById(Long id);

    EventDto validateEvent(Event event);

    EventDto refuseEvent(Event event);

    Page<Event> findAllByDate(int year, int month, Pageable pageable);

    Event planifyEvent(Event event, LocalDateTime dateTime);
}
