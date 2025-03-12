package org.seba.eventrack.api.models.security.forms;

public record OtpForm(
        String email,
        String password,
        String otpCode
) {
    public OtpForm toOtpForm() {
        return new OtpForm(email, password, otpCode);
    }
}
