package com.nisum.test.msuser.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnknownErrorException extends RuntimeException{

    private String description;

    public UnknownErrorException(final String message, final Exception exception) {
        super(message, exception);
    }

    public UnknownErrorException(final String message) {
        super(message);
        this.description = message;
    }
}
