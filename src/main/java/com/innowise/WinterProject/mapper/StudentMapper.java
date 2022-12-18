package com.innowise.WinterProject.mapper;

import com.innowise.WinterProject.dto.StudentDto;
import com.innowise.WinterProject.entity.Student;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface StudentMapper {
    StudentDto studentToDto(Student student);
    Student dtoToStudent(StudentDto studentDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Student updateStudent(Student studentAfterChanges, @MappingTarget Student studentBeforeChanges );

}