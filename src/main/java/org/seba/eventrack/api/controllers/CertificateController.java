package org.seba.eventrack.api.controllers;

import lombok.RequiredArgsConstructor;
import org.seba.eventrack.bll.services.CertificateService;
import org.seba.eventrack.dl.entities.Certificate;
import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
public class CertificateController {
    private final CertificateService certificateService;

    @PostMapping("/generate")
    public ResponseEntity<Certificate> generateCertificate(@RequestParam User userId, @RequestParam Event eventId) {
        return ResponseEntity.ok(certificateService.generateCertificate(userId, eventId));
    }
}
