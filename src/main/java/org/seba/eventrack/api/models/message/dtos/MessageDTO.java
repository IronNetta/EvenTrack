package org.seba.eventrack.api.models.message.dtos;

import org.seba.eventrack.api.models.user.dtos.UserDTO;
import org.seba.eventrack.dl.entities.Message;
import org.seba.eventrack.dl.enums.MessageStatus;

import java.time.LocalDateTime;

public record MessageDTO(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UserDTO sender,
        UserDTO receiver,
        String content,
        MessageStatus messageStatus
) {
    public static MessageDTO fromMessage(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                UserDTO.fromUser(message.getSender()),
                UserDTO.fromUser(message.getReceiver()),
                message.getContent(),
                message.getStatus()
        );
    }
}
