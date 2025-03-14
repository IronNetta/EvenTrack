package org.seba.eventrack.api.controllers;

import lombok.RequiredArgsConstructor;
import org.seba.eventrack.bll.services.BadgeService;
import org.seba.eventrack.dl.entities.Badge;
import org.seba.eventrack.dl.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/badges")
@RequiredArgsConstructor
public class BadgeController {
    private final BadgeService badgeService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Badge>> getUserBadges(@PathVariable User userId) {
        return ResponseEntity.ok(badgeService.getUserBadges(userId));
    }
}