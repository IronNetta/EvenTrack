package org.seba.eventrack.api.controllers.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.seba.eventrack.api.models.security.dtos.UserSessionDTO;
import org.seba.eventrack.api.models.security.dtos.UserTokenDTO;
import org.seba.eventrack.api.models.security.forms.LoginForm;
import org.seba.eventrack.api.models.security.forms.OtpForm;
import org.seba.eventrack.api.models.security.forms.RegisterForm;
import org.seba.eventrack.bll.services.security.AuthService;
import org.seba.eventrack.bll.services.security.TwoFactorAuthService;
import org.seba.eventrack.dl.entities.User;
import org.seba.eventrack.il.utils.jwt.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final TwoFactorAuthService twoFactorAuthService;
    private final JwtUtil jwtUtil;

    @PreAuthorize("isAnonymous()")
    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterForm form
    ) {
        authService.register(form.toUser());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    public ResponseEntity<UserTokenDTO> login(
            @Valid @RequestBody LoginForm form
    ) {
        User user = authService.login(form.email(), form.password());
        UserSessionDTO userDTO = UserSessionDTO.fromUser(user);
        String token = jwtUtil.generateToken(user);
        UserTokenDTO userTokenDTO;
        if (user.isTwoFactorEnabled()) {
            twoFactorAuthService.sendOTPEmail(user);
            userTokenDTO = new UserTokenDTO(userDTO, "OTP envoyé par email (2FA activé)");
        }else{
            userTokenDTO = new UserTokenDTO(userDTO, token);
        }
        return ResponseEntity.ok(userTokenDTO);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<UserTokenDTO> verifyOTP(@RequestBody OtpForm request) {
        User userConfirm = (User) authService.loadUserByUsername(request.email());

        if (userConfirm == null || !twoFactorAuthService.verifyOTP(userConfirm, request.otpCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired otp code");
        }


        User user = authService.login(request.email(), request.password());
        UserSessionDTO userDTO = UserSessionDTO.fromUser(user);
        String token = jwtUtil.generateToken(user);
        UserTokenDTO userTokenDTO = new UserTokenDTO(userDTO, token);

        return ResponseEntity.ok(userTokenDTO);
    }
}