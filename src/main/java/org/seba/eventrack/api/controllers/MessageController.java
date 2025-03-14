package org.seba.eventrack.api.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.seba.eventrack.api.models.message.dtos.MessageDTO;
import org.seba.eventrack.api.models.user.dtos.UserDTO;
import org.seba.eventrack.bll.services.MessageService;
import org.seba.eventrack.bll.services.UserService;
import org.seba.eventrack.dl.entities.Message;
import org.seba.eventrack.dl.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "Endpoints pour la gestion des messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/send")
    public ResponseEntity<MessageDTO> sendMessage(
            @AuthenticationPrincipal User sender,
            @RequestParam Long receiverId,
            @RequestParam String content
    ) {
        User receiver = userService.getUserById(receiverId);
        return ResponseEntity.ok(MessageDTO.fromMessage(messageService.sendMessage(sender, receiver, content)));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/received")
    public ResponseEntity<List<MessageDTO>> getReceivedMessages(@AuthenticationPrincipal User receiver) {
        return ResponseEntity.ok(messageService.getReceivedMessages(receiver).stream().map(MessageDTO::fromMessage).toList());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/sent")
    public ResponseEntity<List<MessageDTO>> getSentMessages(@AuthenticationPrincipal User sender) {
        return ResponseEntity.ok(messageService.getSentMessages(sender).stream().map(MessageDTO::fromMessage).toList());
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        messageService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}