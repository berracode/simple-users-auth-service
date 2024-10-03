package com.nisum.test.msuser.mapper;

import com.nisum.test.msuser.dtos.UserDto;
import com.nisum.test.msuser.entities.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

/**
 * UserMapper.
 *
 * @author Juan Hernandez.
 * @version 1.0.0, 03-10-2024
 */
@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface UserMapper extends EntityMapper<UserDto, User> {

}