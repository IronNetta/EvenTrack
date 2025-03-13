package org.seba.eventrack.dal.repositories;

import org.seba.eventrack.dl.entities.Certificate;
import org.seba.eventrack.dl.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByUser(User user);
}
