package com.nisum.test.msuser.dtos;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserDto.
 *
 * @author Juan Hernandez.
 * @version 1.0.0, 03-10-2024
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String token;
    private Boolean isActive;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private LocalDateTime lastLogin;
}