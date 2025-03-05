package org.seba.eventrack.dal.repositories;

import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    Optional<Ticket> findByQrCodeUrl(String qrCodeUrl);

    List<Ticket> findByEvent(Event event);

    @Query("SELECT t.createdAt FROM Ticket t WHERE t.id = :id")
    LocalDateTime getCreationDate(Long id);
}
