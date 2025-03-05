package org.seba.eventrack.api.models.user.forms;

import org.seba.eventrack.dl.entities.User;

public record UserForm(
    String username,
    String email,
    String password
) {
    public User toUser() {
        return new User(
            username,
            email,
            password
        );
    }
}
