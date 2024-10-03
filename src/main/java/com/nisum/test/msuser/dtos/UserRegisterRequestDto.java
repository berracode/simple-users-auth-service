package com.nisum.test.msuser.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Set;
import lombok.Data;

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
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\d])[A-Za-z\\d\\W]{8,}$")
    private String password;

    @NotNull
    private Set<PhoneDto> phones;
}