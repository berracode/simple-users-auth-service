package com.nisum.test.msuser.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 * DataDuplicatedException.
 *
 * @author Juan Hernandez.
 * @version 1.0.0, 03-10-2024
 */
@Getter
@Setter
public class DataDuplicatedException extends RuntimeException {
    private String description;

    public DataDuplicatedException(final String message, final Exception exception) {
        super(message, exception);
    }

    public DataDuplicatedException(final String message) {
        super(message);
        this.description = message;
    }

}