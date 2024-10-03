package com.nisum.test.msuser.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * UserLoginDto.
 *
 * @author Juan Hernandez.
 * @version 1.0.0, 03-10-2024
 */
@Data
public class UserLoginDto {
    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;
}