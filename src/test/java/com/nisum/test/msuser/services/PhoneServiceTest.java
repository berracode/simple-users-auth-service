package com.nisum.test.msuser.services;

import com.nisum.test.msuser.entities.Phone;
import com.nisum.test.msuser.exceptions.DataConstraintViolationException;
import com.nisum.test.msuser.exceptions.UnknownErrorException;
import com.nisum.test.msuser.repositories.PhoneRepository;
import com.nisum.test.msuser.services.impl.PhoneServiceImpl;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.nisum.test.msuser.constants.Constants.COULD_NOT_SAVE_DATA;
import static com.nisum.test.msuser.constants.Constants.UNKNOWN_SERVER_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

public class PhoneServiceTest {
    @Mock
    private PhoneRepository phoneRepository;

    @InjectMocks
    private PhoneServiceImpl phoneService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveAll_Success() { //Usando patron AAA
        // Arrange
        var userId = UUID.randomUUID();
        Phone phone1 = new Phone();
        phone1.setId(UUID.randomUUID());
        phone1.setNumber("123456");
        phone1.setCityCode("01");
        phone1.setCountryCode("51");
        phone1.setUserId(userId);

        Phone phone2 = new Phone();
        phone2.setId(UUID.randomUUID());
        phone2.setNumber("455666");
        phone2.setCityCode("02");
        phone2.setCountryCode("51");
        phone2.setUserId(userId);

        Set<Phone> phones = new HashSet<>();
        phones.add(phone1);
        phones.add(phone2);

        // Act
        when(phoneRepository.saveAll(anySet())).thenReturn(List.of(phone1, phone2));

        Set<Phone> result = phoneService.saveAll(phones);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        Mockito.verify(phoneRepository, times(1)).saveAll(phones);
    }

    @Test
    void testSaveAll_DataIntegrityViolation() {
        Set<Phone> phones = new HashSet<>();

        when(phoneRepository.saveAll(anySet())).thenThrow(new DataIntegrityViolationException("Constraint violation"));

        DataConstraintViolationException exception = assertThrows(DataConstraintViolationException.class, () -> {
            phoneService.saveAll(phones);
        });

        assertEquals(COULD_NOT_SAVE_DATA, exception.getMessage());
        Mockito.verify(phoneRepository, times(1)).saveAll(phones);
    }

    @Test
    void testSaveAll_UnknownError() {
        Set<Phone> phones = new HashSet<>();

        when(phoneRepository.saveAll(anySet())).thenThrow(new RuntimeException("Unknown error"));

        UnknownErrorException exception = assertThrows(UnknownErrorException.class, () -> {
            phoneService.saveAll(phones);
        });

        assertEquals(UNKNOWN_SERVER_ERROR, exception.getMessage());
        Mockito.verify(phoneRepository, times(1)).saveAll(phones);
    }

}
