package org.seba.eventrack.dal.repositories;

import org.seba.eventrack.dl.entities.Message;
import org.seba.eventrack.dl.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiverOrderByCreatedAtDesc(User receiver);
    List<Message> findBySenderOrderByCreatedAtDesc(User sender);
}
