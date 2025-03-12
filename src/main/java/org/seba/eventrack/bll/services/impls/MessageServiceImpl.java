package org.seba.eventrack.bll.services.impls;

import lombok.RequiredArgsConstructor;
import org.seba.eventrack.bll.services.MessageService;
import org.seba.eventrack.dal.repositories.MessageRepository;
import org.seba.eventrack.dl.entities.Message;
import org.seba.eventrack.dl.entities.User;
import org.seba.eventrack.dl.enums.MessageStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public Message sendMessage(User sender, User receiver, String content) {
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getReceivedMessages(User receiver) {
        return messageRepository.findByReceiverOrderByTimestampDesc(receiver);
    }

    @Override
    public List<Message> getSentMessages(User sender) {
        return messageRepository.findBySenderOrderByTimestampDesc(sender);
    }

    @Override
    public void markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found"));
        message.setStatus(MessageStatus.READ);
        messageRepository.save(message);
    }

    @Override
    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }
}