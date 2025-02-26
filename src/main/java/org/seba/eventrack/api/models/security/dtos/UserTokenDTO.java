package org.seba.eventrack.api.models.security.dtos;

public record UserTokenDTO(
        UserSessionDTO user,
        String token
) {
}
