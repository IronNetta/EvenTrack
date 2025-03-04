package org.seba.eventrack.bll.services.security.impl;

import lombok.RequiredArgsConstructor;
import org.seba.eventrack.api.models.mails.dtos.EmailsDTO;
import org.seba.eventrack.bll.exceptions.user.BadCredentialsException;
import org.seba.eventrack.bll.exceptions.user.UserNotFoundException;
import org.seba.eventrack.bll.services.mails.EmailService;
import org.seba.eventrack.bll.services.security.AuthService;
import org.seba.eventrack.dal.repositories.UserRepository;
import org.seba.eventrack.dl.entities.User;
import org.seba.eventrack.dl.enums.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public void register(User user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            //Todo Gestion d'exception cleaner
            throw new UserNotFoundException(HttpStatus.NOT_ACCEPTABLE, "Bad credentials");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.PARTICIPANT);
        //Todo creation et validation de code
        emailService.sendSimpleMail(new EmailsDTO(user.getEmail(), "Votre code de validation est le suivant", "Bienvenue sur Eventrack"));
        //Todo Handle image
        userRepository.save(user);
    }


    @Override
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(HttpStatus.NOT_FOUND, "User with email " + email + " not found")
        );
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException(HttpStatus.NOT_ACCEPTABLE, "Bad credentials");
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(HttpStatus.NOT_FOUND, email)
        );
    }
}