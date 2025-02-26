package org.seba.eventrack.api.models.user.dtos;


import org.seba.eventrack.dl.entities.User;

public record UserDTO(
        Long id,
        String username
) {

    public static UserDTO fromUser(User user) {
        return new UserDTO(user.getId(), user.getUsername());
    }
}
