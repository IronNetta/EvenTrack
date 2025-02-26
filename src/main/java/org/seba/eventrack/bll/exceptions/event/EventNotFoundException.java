package org.seba.eventrack.bll.exceptions.event;

import org.seba.eventrack.bll.exceptions.GlobalException;
import org.springframework.http.HttpStatus;

public class EventNotFoundException extends GlobalException {

    public EventNotFoundException(HttpStatus status, Object error) {
        super(status, error);
    }
}
