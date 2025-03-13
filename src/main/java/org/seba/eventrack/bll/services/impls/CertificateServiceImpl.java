package org.seba.eventrack.bll.services.impls;

import lombok.RequiredArgsConstructor;
import org.seba.eventrack.bll.services.CertificateService;
import org.seba.eventrack.dal.repositories.CertificateRepository;
import org.seba.eventrack.dl.entities.Certificate;
import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.entities.User;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;

    @Override
    public Certificate generateCertificate(User user, Event event) {
        String filePath = "certificates/" + user.getId() + "_" + event.getId() + ".pdf";
        generatePdf(filePath, user, event);

        Certificate certificate = new Certificate(user, event, filePath, LocalDateTime.now());
        return certificateRepository.save(certificate);
    }

    @Override
    public List<Certificate> getUserCertificates(User user) {
        return certificateRepository.findByUser(user);
    }

    private void generatePdf(String filePath, User user, Event event) {
        try {
            String content = "Certificat de participation\n\n" +
                    "Nom : " + user.getUsername() + "\n" +
                    "Événement : " + event.getTitle() + "\n" +
                    "Date : " + event.getDate();

            Files.write(Path.of(filePath), content.getBytes(), StandardOpenOption.CREATE);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du certificat", e);
        }
    }
}
