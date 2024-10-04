package com.nisum.test.msuser.security;

import com.nisum.test.msuser.entities.User;
import com.nisum.test.msuser.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
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
    void testLogin_Success() throws Exception {
        String loginRequest = """
                {
                    "email": "test@test.com",
                    "password": "password"
                }
                """;

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk());
    }

    @Test
    void testAccessProtectedEndpoint_Unauthorized() throws Exception {
        mockMvc.perform(post("/protected-endpoint"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void testAccessProtectedEndpoint_Authorized() throws Exception {
        mockMvc.perform(post("/users/test-private-endpoint"))
                .andExpect(status().isOk());
    }
}
