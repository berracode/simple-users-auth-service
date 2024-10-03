package com.nisum.test.msuser.repositories;

import com.nisum.test.msuser.entities.Phone;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, UUID> {

}