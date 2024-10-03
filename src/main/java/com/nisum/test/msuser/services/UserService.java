package com.nisum.test.msuser.services;

import com.nisum.test.msuser.entities.User;
import java.util.UUID;

public interface UserService {

    User save(User user);
    User update(User user);
    User findById(UUID id);
    User findByEmail(String email);
    void verifyUserExistence(String email);
}