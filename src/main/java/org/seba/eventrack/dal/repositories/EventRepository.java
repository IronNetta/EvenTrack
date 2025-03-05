package org.seba.eventrack.dal.repositories;

import org.seba.eventrack.dl.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    Event findByTitle(String title);

    @Query("SELECT e.reservedSeats * 100 / e.capacity FROM Event e WHERE e.id = :eventId")
    Double getRatioOfReservedSeats(@Param("eventId")Long eventId);

    @Query("SELECT e.reservedSeats FROM Event e WHERE e.id = :eventId")
    Double getNumberOfReservedSeats(Long eventId);

    @Query("SELECT e.createdAt FROM Event e WHERE e.id = :eventId")
    LocalDateTime getCreationDate(Long eventId);
}
