package com.nisum.test.msuser.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataNotFoundException  extends RuntimeException {

    private String description;

    public DataNotFoundException(final String message) {
        super(message);
        this.description = message;
    }

    public DataNotFoundException(final String message, final Exception exception) {
        super(message, exception);
    }

}