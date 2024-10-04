package com.nisum.test.msuser.services.impl;

import com.nisum.test.msuser.entities.Phone;
import com.nisum.test.msuser.exceptions.DataConstraintViolationException;
import com.nisum.test.msuser.exceptions.UnknownErrorException;
import com.nisum.test.msuser.repositories.PhoneRepository;
import com.nisum.test.msuser.services.PhoneService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.Set;

import static com.nisum.test.msuser.constants.Constants.COULD_NOT_SAVE_DATA;
import static com.nisum.test.msuser.constants.Constants.UNKNOWN_SERVER_ERROR;

@Service
@Transactional
@Validated
public class PhoneServiceImpl implements PhoneService {

    private final PhoneRepository phoneRepository;


    public PhoneServiceImpl(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    @Override
    public Set<Phone> saveAll(Set<Phone> phones) {

        try{
            var phonesList = phoneRepository.saveAll(phones);
            var phoneSet = new HashSet<>(phonesList);
            return phoneSet;
        } catch (DataIntegrityViolationException ex){
            throw new DataConstraintViolationException(COULD_NOT_SAVE_DATA);
        } catch (Exception ex){
            throw new UnknownErrorException(UNKNOWN_SERVER_ERROR);
        }
    }
}
