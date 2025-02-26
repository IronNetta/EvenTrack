package org.seba.eventrack.bll.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Schema(description = "Format de réponse en cas d'erreur")
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public abstract class GlobalException extends RuntimeException{

    @Schema(description = "Code HTTP de l'erreur")
    private final HttpStatus status;
    @Schema(description = "Message détaillé de l'erreur")
    private final Object error;

    public GlobalException(HttpStatus status, Object error) {
        this.status = status;
        this.error = error;
    }
}
