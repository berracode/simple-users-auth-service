package com.nisum.test.msuser.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PhoneDto.
 *
 * @author Juan Hernandez.
 * @version 1.0.0, 03-10-2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDto {
    @NotNull
    private String number;

    @NotNull
    private String cityCode;

    @NotNull
    private String countryCode;
}