package org.seba.eventrack.api.controllers.advisor;

import io.swagger.v3.oas.annotations.Hidden;
import org.seba.eventrack.bll.exceptions.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Hidden
@RestControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<Object> handleException(GlobalException e) {
        return ResponseEntity.status(e.getStatus()).body(e.getError());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You dont have access to this resource");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleException(MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach((error) -> {
            errors.put(error.getField(),error.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
