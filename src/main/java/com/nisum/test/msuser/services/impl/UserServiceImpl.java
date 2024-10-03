package com.nisum.test.msuser.services.impl;

import com.nisum.test.msuser.entities.User;
import com.nisum.test.msuser.exceptions.DataDuplicatedException;
import com.nisum.test.msuser.exceptions.DataNotFoundException;
import com.nisum.test.msuser.repositories.UserRepository;
import com.nisum.test.msuser.services.UserService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import static com.nisum.test.msuser.constants.Constants.USER_DUPLICATED;
import static com.nisum.test.msuser.constants.Constants.USER_NOT_FOUND;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public User save(User user) {
        user.setCreatedDate(LocalDateTime.now());
        user.setModifiedDate(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        return repository.save(user);
    }

    public User update(User user) {
        user.setModifiedDate(LocalDateTime.now());
        return repository.save(user);
    }

    public User findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new DataNotFoundException(USER_NOT_FOUND));
    }
    public User findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() ->
            new DataNotFoundException(USER_NOT_FOUND));
    }

    public void verifyUserExistence(String email) {
        var user = repository.findByEmail(email);
        if(user.isPresent()){
            throw new DataDuplicatedException(USER_DUPLICATED);
        }
    }


}