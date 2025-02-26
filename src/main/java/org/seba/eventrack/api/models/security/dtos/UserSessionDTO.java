package org.seba.eventrack.api.models.security.dtos;


import org.seba.eventrack.dl.entities.User;
import org.seba.eventrack.dl.enums.UserRole;

public record UserSessionDTO(
        Long id,
        UserRole role,
        String userName
) {

    public static UserSessionDTO fromUser(User user) {
        return new UserSessionDTO(user.getId(),user.getRole(),user.getUsername());
    }
}
