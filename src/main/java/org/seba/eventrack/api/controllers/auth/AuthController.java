package org.seba.eventrack.api.controllers.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.seba.eventrack.api.models.security.dtos.UserSessionDTO;
import org.seba.eventrack.api.models.security.dtos.UserTokenDTO;
import org.seba.eventrack.api.models.security.forms.LoginForm;
import org.seba.eventrack.api.models.security.forms.RegisterForm;
import org.seba.eventrack.bll.services.security.AuthService;
import org.seba.eventrack.dl.entities.User;
import org.seba.eventrack.il.utils.jwt.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
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
        UserTokenDTO userTokenDTO = new UserTokenDTO(userDTO, token);
        return ResponseEntity.ok(userTokenDTO);
    }
}