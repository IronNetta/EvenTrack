package org.seba.eventrack.dal.repositories;

import org.seba.eventrack.dl.entities.Badge;
import org.seba.eventrack.dl.entities.User;
import org.seba.eventrack.dl.enums.BadgeType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findByUser(User user);
    boolean existsByUserAndType(User user, BadgeType type);
}
