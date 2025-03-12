package org.seba.eventrack.api.controllers;

import lombok.RequiredArgsConstructor;
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
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(
            @AuthenticationPrincipal User sender,
            @RequestParam Long receiverId,
            @RequestParam String content
    ) {
        User receiver = userService.getUserById(receiverId);
        return ResponseEntity.ok(messageService.sendMessage(sender, receiver, content));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/received")
    public ResponseEntity<List<Message>> getReceivedMessages(@AuthenticationPrincipal User receiver) {
        return ResponseEntity.ok(messageService.getReceivedMessages(receiver));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/sent")
    public ResponseEntity<List<Message>> getSentMessages(@AuthenticationPrincipal User sender) {
        return ResponseEntity.ok(messageService.getSentMessages(sender));
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