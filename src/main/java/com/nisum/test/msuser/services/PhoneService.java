package com.nisum.test.msuser.services;

import com.nisum.test.msuser.entities.Phone;

import java.util.Set;

public interface PhoneService {

    Set<Phone> saveAll(Set<Phone> phones);
}
