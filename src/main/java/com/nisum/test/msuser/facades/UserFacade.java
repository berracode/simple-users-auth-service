package com.nisum.test.msuser.facades;

import com.nisum.test.msuser.dtos.UserDto;
import com.nisum.test.msuser.dtos.UserRegisterRequestDto;
import com.nisum.test.msuser.entities.Phone;
import com.nisum.test.msuser.mapper.UserMapper;
import com.nisum.test.msuser.services.PhoneService;
import com.nisum.test.msuser.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * UserFacade.
 *
 * @author Juan Hernandez.
 * @version 1.0.0, 03-10-2024
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;
    private final PhoneService phoneService;
    private final UserMapper userMapper;

    public UserDto registerUser(final UserRegisterRequestDto userRegisterRequestDto) {

        userService.verifyUserExistence(userRegisterRequestDto.getEmail());

        var userDto =
                UserDto.builder().name(userRegisterRequestDto.getName()).email(userRegisterRequestDto.getEmail()).password(
                        new BCryptPasswordEncoder().encode(userRegisterRequestDto.getPassword())).isActive(Boolean.TRUE).build();

        var userCreated = userMapper.toDto(userService.save(userMapper.toEntity(userDto)));

        if (Objects.nonNull(userRegisterRequestDto.getPhones())) {
            var phones = userRegisterRequestDto.getPhones();
            var phoneWithUser = phones.stream().map(phoneDto -> Phone.builder()
                    .number(phoneDto.getNumber())
                    .cityCode(phoneDto.getCityCode())
                    .countryCode(phoneDto.getCountryCode())
                    .userId(userCreated.getId())
                    .build()
            ).collect(Collectors.toSet());
            phoneService.saveAll(phoneWithUser);
        }

        return userCreated;
    }
}