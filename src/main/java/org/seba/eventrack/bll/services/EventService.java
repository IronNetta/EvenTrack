package org.seba.eventrack.bll.services;

import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.entities.User;
import org.seba.eventrack.il.requests.SearchParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EventService {

    public Page<Event> findAll(List<SearchParam<Event>> searchParams, Pageable pageable);

    public Event save(Event event);

    public Event findById(Long id);

    public Event update(Event event);

    public void deleteById(Long id);
}
