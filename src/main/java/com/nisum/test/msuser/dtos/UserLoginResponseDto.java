package com.nisum.test.msuser.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * UserLoginResponseDto.
 *
 * @author Juan Hernandez.
 * @version 1.0.0, 03-10-2024
 */
@Getter
@Setter
@Builder
public class UserLoginResponseDto {
    private String token;
    private String tokenType;
}