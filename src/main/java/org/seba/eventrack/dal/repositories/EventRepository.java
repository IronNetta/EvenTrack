package org.seba.eventrack.dal.repositories;

import org.seba.eventrack.dl.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
