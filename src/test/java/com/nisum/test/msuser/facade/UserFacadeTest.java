package com.nisum.test.msuser.facade;

import com.nisum.test.msuser.dtos.PhoneDto;
import com.nisum.test.msuser.dtos.UserDto;
import com.nisum.test.msuser.dtos.UserRegisterRequestDto;
import com.nisum.test.msuser.entities.User;
import com.nisum.test.msuser.exceptions.DataNotFoundException;
import com.nisum.test.msuser.facades.UserFacade;
import com.nisum.test.msuser.mapper.UserMapper;
import com.nisum.test.msuser.services.PhoneService;
import com.nisum.test.msuser.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;
import java.util.UUID;

import static com.nisum.test.msuser.constants.Constants.USER_DUPLICATED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserFacadeTest {

    @Mock
    private UserService userService;

    @Mock
    private PhoneService phoneService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserFacade userFacade;

    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void registerUser_Success_NoPhones() {
        // arrange
        UserRegisterRequestDto requestDto = new UserRegisterRequestDto();
        requestDto.setName("Test User");
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("password");
        requestDto.setPhones(null);

        UserDto expectedUserDto = UserDto.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email("test@example.com")
                .password(passwordEncoder.encode("password"))
                .isActive(Boolean.TRUE)
                .build();

        // act
        doNothing().when(userService).verifyUserExistence(requestDto.getEmail());
        when(userMapper.toEntity(any(UserDto.class))).thenReturn(new User());
        when(userService.save(any(User.class))).thenReturn(new User());
        when(userMapper.toDto(any(User.class))).thenReturn(expectedUserDto);

        UserDto result = userFacade.registerUser(requestDto);

        assertNotNull(result);
        assertEquals(expectedUserDto.getEmail(), result.getEmail());
        verify(userService, times(1)).verifyUserExistence(requestDto.getEmail());
        verify(userMapper, times(1)).toEntity(any(UserDto.class));
        verify(userService, times(1)).save(any(User.class));
        verify(phoneService, never()).saveAll(anySet());
    }

    @Test
    void registerUser_Success_WithPhones() {
        // Arrange
        PhoneDto phoneDto1 = new PhoneDto("123456", "01", "51");
        PhoneDto phoneDto2 = new PhoneDto("654321", "02", "51");

        UserRegisterRequestDto requestDto = new UserRegisterRequestDto();
        requestDto.setName("Test User");
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("password");
        requestDto.setPhones(Set.of(phoneDto1, phoneDto2));

        UserDto expectedUserDto = UserDto.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email("test@example.com")
                .password(passwordEncoder.encode("password"))
                .isActive(Boolean.TRUE)
                .build();
        // act
        doNothing().when(userService).verifyUserExistence(requestDto.getEmail());
        when(userMapper.toEntity(any(UserDto.class))).thenReturn(new User());
        when(userService.save(any(User.class))).thenReturn(new User());
        when(userMapper.toDto(any(User.class))).thenReturn(expectedUserDto);

        when(phoneService.saveAll(anySet())).thenReturn(Set.of());

        UserDto result = userFacade.registerUser(requestDto);

        assertNotNull(result);
        assertEquals(expectedUserDto.getEmail(), result.getEmail());
        verify(userService, times(1)).verifyUserExistence(requestDto.getEmail());
        verify(userMapper, times(1)).toEntity(any(UserDto.class));
        verify(userService, times(1)).save(any(User.class));
        verify(phoneService, times(1)).saveAll(anySet());
    }

    @Test
    void registerUser_UserAlreadyExists() {
        UserRegisterRequestDto requestDto = new UserRegisterRequestDto();
        requestDto.setEmail("test@email.com");

        doThrow(new DataNotFoundException(USER_DUPLICATED))
                .when(userService).verifyUserExistence(requestDto.getEmail());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> userFacade.registerUser(requestDto));

        assertEquals(USER_DUPLICATED, exception.getMessage());
        verify(userService, times(1)).verifyUserExistence(requestDto.getEmail());
        verify(userService, never()).save(any(User.class));
    }
}
