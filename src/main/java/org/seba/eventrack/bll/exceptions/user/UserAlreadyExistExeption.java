package org.seba.eventrack.bll.exceptions.user;

import org.seba.eventrack.bll.exceptions.GlobalException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistExeption extends GlobalException {
    public UserAlreadyExistExeption(HttpStatus status, Object error) {
        super(status, error);
    }
}
