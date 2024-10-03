package com.nisum.test.msuser.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * PhoneDto.
 *
 * @author Juan Hernandez.
 * @version 1.0.0, 03-10-2024
 */
@Data
public class PhoneDto {
    @NotNull
    private String number;

    @NotNull
    private String cityCode;

    @NotNull
    private String countryCode;
}