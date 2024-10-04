package com.nisum.test.msuser.dtos;

import com.nisum.test.msuser.utils.annotation.ValidEmail;
import com.nisum.test.msuser.utils.annotation.ValidPass;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

/**
 * UserRegisterDto.
 *
 * @author Juan Hernandez.
 * @version 1.0.0, 03-10-2024
 */
@Data
public class UserRegisterRequestDto {
    @NotNull
    private String name;

    @NotNull
    @ValidEmail
    private String email;

    @NotNull
    @ValidPass
    private String password;

    @NotNull
    @Valid
    private Set<PhoneDto> phones;
}