package com.nisum.test.msuser.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataConstraintViolationException extends RuntimeException {

    private String description;

    public DataConstraintViolationException(final String message, final Exception exception) {
        super(message, exception);
    }

    public DataConstraintViolationException(final String message) {
        super(message);
        this.description = message;
    }
}
