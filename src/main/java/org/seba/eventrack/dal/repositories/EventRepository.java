package org.seba.eventrack.dal.repositories;

import org.seba.eventrack.dl.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    Event findByTitle(String title);

    @Query("select e from Event e where year(e.date) = :year and month(e.date) = :month")
    Page<Event> findByDate(int year, int month, Pageable pageable);
}
