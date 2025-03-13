package org.seba.eventrack.bll.services;

import org.seba.eventrack.dl.entities.Badge;
import org.seba.eventrack.dl.entities.User;

import java.util.List;

public interface BadgeService {
    void checkAndAwardBadges(User user);
    List<Badge> getUserBadges(User user);
}