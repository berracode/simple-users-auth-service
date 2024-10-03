package com.nisum.test.msuser.facades;

import com.nisum.test.msuser.dtos.UserDto;
import com.nisum.test.msuser.dtos.UserRegisterRequestDto;
import com.nisum.test.msuser.dtos.UserRegisterResponseDto;
import com.nisum.test.msuser.mapper.UserMapper;
import com.nisum.test.msuser.services.UserService;
import jakarta.transaction.Transactional;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(UserFacade.class);
    private final UserService userService;
    private final UserMapper userMapper;


    public UserDto registerUser(final UserRegisterRequestDto userRegisterRequestDto) {

        userService.verifyUserExistence(userRegisterRequestDto.getEmail());

        var userDto =
            UserDto.builder().name(userRegisterRequestDto.getName()).email(userRegisterRequestDto.getEmail()).password(
                new BCryptPasswordEncoder().encode(userRegisterRequestDto.getPassword())).isActive(Boolean.TRUE).build();

        var userCreated = userMapper.toDto(userService.save(userMapper.toEntity(userDto)));

        return  userCreated;
    }

}