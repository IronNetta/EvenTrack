package org.seba.eventrack.bll.services.mails.impls;

import jakarta.mail.MessagingException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.seba.eventrack.api.models.mails.dtos.EmailsDTO;
import org.seba.eventrack.bll.services.EventService;
import org.seba.eventrack.bll.services.UserService;
import org.seba.eventrack.bll.services.impls.TicketServiceImpl;
import org.seba.eventrack.bll.services.mails.EmailService;
import org.seba.eventrack.bll.services.mails.EventReminderService;
import org.seba.eventrack.dal.repositories.TicketRepository;
import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.entities.Ticket;
import org.seba.eventrack.dl.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventReminderServiceImpl implements EventReminderService {

    private final EventService eventService;
    private final TicketRepository ticketRepository;
    private final EmailService emailService;

    @Override
    public String sendEventReminder(EmailsDTO details) {

        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        List<Event> events = eventService.findAll(List.of(), Pageable.unpaged()).getContent()
                .stream()
                .filter(e -> e.getDate().isAfter(tomorrow))
                .toList();

        for (Event event : events) {
            List<Ticket> tickets = ticketRepository.findByEvent(event);

            for (Ticket ticket : tickets) {
                User user = ticket.getParticipant();

                try {
                    emailService.sendEventReminderEmail(user, event);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }

        return "Emails envoyés";
    }
}
