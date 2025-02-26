package org.seba.eventrack.bll.services;

import org.seba.eventrack.dl.entities.Ticket;
import org.seba.eventrack.dl.entities.User;
import org.seba.eventrack.il.requests.SearchParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TicketService  {

    Page<Ticket> findAll(List<SearchParam<Ticket>> searchParams, Pageable pageable);

    Ticket save(Ticket ticket);

    Ticket findById(Long id);

    Ticket findByQrCodeUrl(String qrCodeUrl);

    Ticket update(Ticket ticket);

    void deleteById(Long id);
}
