package com.nisum.test.msuser.services;

import com.nisum.test.msuser.entities.User;
import com.nisum.test.msuser.exceptions.DataDuplicatedException;
import com.nisum.test.msuser.exceptions.DataNotFoundException;
import com.nisum.test.msuser.repositories.UserRepository;
import com.nisum.test.msuser.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static com.nisum.test.msuser.constants.Constants.USER_DUPLICATED;
import static com.nisum.test.msuser.constants.Constants.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_Success() {
        // Datos de prueba
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");

        // Simulamos el comportamiento del repositorio
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(UUID.randomUUID());
            return savedUser;
        });

        // Llamada al método de prueba
        User result = userService.save(user);

        // Verificaciones
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(user.getName(), result.getName());
        assertNotNull(result.getCreatedDate());
        assertNotNull(result.getModifiedDate());
        assertNotNull(result.getLastLogin());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void update_Success() {
        // Datos de prueba
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Updated User");

        // Simulamos el comportamiento del repositorio
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Llamada al método de prueba
        User result = userService.update(user);

        // Verificaciones
        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        assertNotNull(result.getModifiedDate());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void findById_UserExists() {
        // Datos de prueba
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("Test User");

        // Simulamos el comportamiento del repositorio
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Llamada al método de prueba
        User result = userService.findById(userId);

        // Verificaciones
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(user.getName(), result.getName());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void findById_UserNotFound() {
        // Datos de prueba
        UUID userId = UUID.randomUUID();

        // Simulamos el comportamiento del repositorio
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Llamada al método de prueba y verificación de excepción
        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> userService.findById(userId));

        assertEquals(USER_NOT_FOUND, exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void findByEmail_UserExists() {
        // Datos de prueba
        String email = "test@example.com";
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(email);

        // Simulamos el comportamiento del repositorio
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Llamada al método de prueba
        User result = userService.findByEmail(email);

        // Verificaciones
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void findByEmail_UserNotFound() {
        // Datos de prueba
        String email = "nonexistent@example.com";

        // Simulamos el comportamiento del repositorio
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Llamada al método de prueba y verificación de excepción
        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> userService.findByEmail(email));

        assertEquals(USER_NOT_FOUND, exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void verifyUserExistence_UserExists() {
        // Datos de prueba
        String email = "test@example.com";

        // Simulamos que el usuario ya existe
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        // Llamada al método de prueba y verificación de excepción
        DataDuplicatedException exception = assertThrows(DataDuplicatedException.class,
                () -> userService.verifyUserExistence(email));

        assertEquals(USER_DUPLICATED, exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void verifyUserExistence_UserDoesNotExist() {
        // Datos de prueba
        String email = "newuser@example.com";

        // Simulamos que el usuario no existe
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Llamada al método de prueba
        userService.verifyUserExistence(email);

        // Verificación
        verify(userRepository, times(1)).findByEmail(email);
        // No debe lanzar ninguna excepción
    }
}

