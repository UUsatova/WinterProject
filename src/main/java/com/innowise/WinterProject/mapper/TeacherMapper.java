package com.innowise.WinterProject.mapper;

import com.innowise.WinterProject.dto.TeacherDto;
import com.innowise.WinterProject.entity.Teacher;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface TeacherMapper {

    Teacher dtoToTeacher(TeacherDto teacherDto);

    TeacherDto teacherToDto(Teacher teacher);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Teacher updateTeacher(Teacher teacherAfterChanges,@MappingTarget Teacher teacherBeforeChanges );

}
