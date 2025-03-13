package org.seba.eventrack.bll.services;

import org.seba.eventrack.dl.entities.Certificate;
import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.entities.User;

import java.util.List;

public interface CertificateService {
    Certificate generateCertificate(User user, Event event);
    List<Certificate> getUserCertificates(User user);
}