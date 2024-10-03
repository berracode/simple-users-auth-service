package com.nisum.test.msuser.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ApiResponse.
 *
 * @author Juan Hernandez.
 * @version 1.0.0, 03-10-2024
 */
@Getter
@Setter
@NoArgsConstructor
public class ApiResponse<T> {

    private String description;
    private String message;
    private T body;

    public ApiResponse(T body, String message) {
        this.message = message;
        this.body = body;
    }

    public ApiResponse(T body) {
        this.body = body;
    }

    public ApiResponse(String message, String description) {
        this.description = description;
        this.message = message;
    }

    public ApiResponse(T body, String message, String description) {
        this.description = description;
        this.message = message;
        this.body = body;
    }

}