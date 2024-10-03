package com.nisum.test.msuser.mapper;

import com.nisum.test.msuser.dtos.PhoneDto;
import com.nisum.test.msuser.entities.Phone;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {UserMapper.class})
public interface PhoneMapper extends EntityMapper<PhoneDto, Phone> {

}

