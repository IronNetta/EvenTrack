package org.seba.eventrack.dal.repositories;

import org.seba.eventrack.dl.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
