package com.innowise.WinterProject.mapper;

import com.innowise.WinterProject.dto.UserAuthDto;
import com.innowise.WinterProject.entity.Teacher;
import com.innowise.WinterProject.entity.User;

public interface UserAuthMapper {
    User dtoToUser(UserAuthDto teacherDto);
    UserAuthDto UserToDto(Teacher teacher);

}
