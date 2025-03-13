package org.seba.eventrack.bll.services.security;

import org.seba.eventrack.dl.entities.User;

public interface TwoFactorAuthService {
    String generateOTP();
    void sendOTPEmail(User user);
    boolean verifyOTP(User user, String otp);
}
