package org.seba.eventrack.bll.exceptions.user;

import org.seba.eventrack.bll.exceptions.GlobalException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends GlobalException {

    public UserNotFoundException(HttpStatus status, Object error) {
        super(status, error);
    }
}
