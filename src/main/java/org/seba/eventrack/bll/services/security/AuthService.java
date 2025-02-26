package org.seba.eventrack.bll.services.security;

import org.seba.eventrack.dl.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService extends UserDetailsService {

    void register(User user);

    User login(String email, String password);
}