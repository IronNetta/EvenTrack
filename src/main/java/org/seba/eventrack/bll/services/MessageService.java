package org.seba.eventrack.bll.services;

import org.seba.eventrack.dl.entities.Message;
import org.seba.eventrack.dl.entities.User;

import java.util.List;

public interface MessageService {
    Message sendMessage(User sender, User receiver, String content);
    List<Message> getReceivedMessages(User receiver);
    List<Message> getSentMessages(User sender);
    void markAsRead(Long messageId);
    void deleteMessage(Long messageId);
}
