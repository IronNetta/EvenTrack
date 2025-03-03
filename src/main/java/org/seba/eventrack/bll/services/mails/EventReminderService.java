package org.seba.eventrack.bll.services.mails;

import org.seba.eventrack.api.models.mails.dtos.EmailsDTO;

public interface EventReminderService {
    String sendEventReminder(EmailsDTO details);
}
