package org.seba.eventrack.api.models.security.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.seba.eventrack.dl.entities.User;

import java.time.LocalDate;

public record RegisterForm(
        @NotBlank @Size(max = 150)
        String email,
        @NotBlank
        String password,
        @NotBlank @Size(max = 50)
        String username
) {

    public User toUser() {
        return new User(
                email,
                password,
                username
        );
    }
}
