package com.innowise.WinterProject.mapper;

import com.innowise.WinterProject.dto.UserDto;
import com.innowise.WinterProject.entity.Teacher;
import com.innowise.WinterProject.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User dtoToUser(UserDto userDto);
    UserDto UserToDto(Teacher user);

}
