package org.seba.eventrack.bll.services.impls;

import lombok.RequiredArgsConstructor;
import org.seba.eventrack.bll.services.BadgeService;
import org.seba.eventrack.dal.repositories.BadgeRepository;
import org.seba.eventrack.dal.repositories.EventRepository;
import org.seba.eventrack.dl.entities.Badge;
import org.seba.eventrack.dl.entities.User;
import org.seba.eventrack.dl.enums.BadgeType;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BadgeServiceImpl implements BadgeService {

    private final BadgeRepository badgeRepository;
    private final EventRepository eventRepository;

    @Override
    public void checkAndAwardBadges(User user) {
        long attendedEvents = eventRepository.countByParticipantsContaining(user);
        long organizedEvents = eventRepository.countByOrganizer(user);

        if (attendedEvents >= 5 && !badgeRepository.existsByUserAndType(user, BadgeType.PARTICIPANT_ASSIDU)) {
            badgeRepository.save(new Badge(BadgeType.PARTICIPANT_ASSIDU, user));
        }

        if (organizedEvents >= 3 && !badgeRepository.existsByUserAndType(user, BadgeType.ORGANISATEUR_CONFIRME)) {
            badgeRepository.save(new Badge(BadgeType.ORGANISATEUR_CONFIRME, user));
        }
    }

    @Override
    public List<Badge> getUserBadges(User user) {
        return badgeRepository.findByUser(user);
    }
}
