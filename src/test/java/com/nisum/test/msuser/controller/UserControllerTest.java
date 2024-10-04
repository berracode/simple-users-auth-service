package com.nisum.test.msuser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nisum.test.msuser.controllers.UserController;
import com.nisum.test.msuser.dtos.PhoneDto;
import com.nisum.test.msuser.dtos.UserDto;
import com.nisum.test.msuser.dtos.UserRegisterRequestDto;
import com.nisum.test.msuser.entities.User;
import com.nisum.test.msuser.facades.UserFacade;
import com.nisum.test.msuser.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserFacade userFacade;

    private UserController userController = mock(UserController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {

        userController = new UserController(userFacade);
        userRepository.deleteAll();

        User user = new User();
        user.setName("Pepito Perez");
        user.setEmail("test@test.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setIsActive(Boolean.TRUE);
        user.setCreatedDate(LocalDateTime.now());
        user.setModifiedDate(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    @Test
    void createUser_Success() throws Exception {
        UserRegisterRequestDto requestDto = new UserRegisterRequestDto();
        requestDto.setName("Test User");
        requestDto.setEmail("test@mail.com");
        requestDto.setPassword("String2024*");
        var phones = Set.of(
                new PhoneDto("123", "6", "57"),
                new PhoneDto("345", "6", "57"));
        requestDto.setPhones(phones);


        UserDto userDto = UserDto.builder()
                .id(UUID.randomUUID())
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .build();

        when(userFacade.registerUser(any(UserRegisterRequestDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void createUser_BadRequest_InvalidInput() throws Exception {
        UserRegisterRequestDto requestDto = new UserRegisterRequestDto();
        requestDto.setName("");

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testMethod_Success() throws Exception {
        mockMvc.perform(post("/users/test-private-endpoint")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User authenticated"))
                .andExpect(jsonPath("$.description").value("If you can see this text, you have a valid access token"));
    }
}

