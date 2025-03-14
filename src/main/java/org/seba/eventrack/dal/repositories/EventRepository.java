package org.seba.eventrack.dal.repositories;

import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;


public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    Event findByTitle(String title);

    @Query("select e from Event e where year(e.date) = :year and month(e.date) = :month")
    Page<Event> findByDate(int year, int month, Pageable pageable);
    
    @Query("SELECT e.reservedSeats * 100 / e.capacity FROM Event e WHERE e.id = :eventId")
    Double getRatioOfReservedSeats(@Param("eventId")Long eventId);

    @Query("SELECT e.reservedSeats FROM Event e WHERE e.id = :eventId")
    Double getNumberOfReservedSeats(Long eventId);

    @Query("SELECT e.createdAt FROM Event e WHERE e.id = :eventId")
    LocalDateTime getCreationDate(Long eventId);

    @Query("SELECT COUNT(e) FROM Event e JOIN e.participants p WHERE p = :user")
    long countByParticipantsContaining(@Param("user") User user);

    @Query("SELECT COUNT(e) FROM Event e WHERE e.organizer = :user")
    long countByOrganizer(@Param("user") User user);
}
