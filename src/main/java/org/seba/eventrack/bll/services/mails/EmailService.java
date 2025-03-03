package org.seba.eventrack.bll.services.mails;

import jakarta.mail.MessagingException;
import org.seba.eventrack.api.models.mails.dtos.EmailsDTO;
import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.entities.User;

public interface EmailService {

    String sendSimpleMail(EmailsDTO details);

    String sendMailWithAttachment(EmailsDTO details);

    String sendEventReminderEmail(User user, Event event) throws MessagingException;
}
