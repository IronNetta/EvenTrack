package org.seba.eventrack.bll.services;

import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.entities.User;
import org.seba.eventrack.il.requests.SearchParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EventService {

    Page<Event> findAll(List<SearchParam<Event>> searchParams, Pageable pageable);

    Event save(Event event);

    Event findById(Long id);

    Event update(Event event);

    void deleteById(Long id);

    Event validateEvent(Event event, User user);

    Event refuseEvent(Event event, User user);
}
