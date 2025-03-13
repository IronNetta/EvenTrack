package org.seba.eventrack.bll.services.security.impl;

import lombok.RequiredArgsConstructor;
import org.seba.eventrack.bll.services.security.TwoFactorAuthService;
import org.seba.eventrack.dal.repositories.UserRepository;
import org.seba.eventrack.dl.entities.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TwoFactorAuthServiceImpl implements TwoFactorAuthService {
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;



    // 🔹 Générer un OTP aléatoire (6 chiffres)
    public String generateOTP() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    // 🔹 Envoyer l'OTP par e-mail
    public void sendOTPEmail(User user) {
        String otp = generateOTP();
        user.setOtp(otp);
        user.setOtpExpiration(LocalDateTime.now().plusMinutes(5)); // Expiration après 5 min
        userRepository.save(user);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Votre code OTP");
        message.setText("Votre code OTP est : " + otp + "\nIl expirera dans 5 minutes.");

        mailSender.send(message);
    }

    // 🔹 Vérifier l'OTP
    public boolean verifyOTP(User user, String otp) {
        return user.getOtp() != null && user.getOtp().equals(otp) &&
                user.getOtpExpiration().isAfter(LocalDateTime.now());
    }
}

