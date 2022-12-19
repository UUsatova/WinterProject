package com.innowise.WinterProject.mapper;

import com.innowise.WinterProject.dto.StudentDto;
import com.innowise.WinterProject.entity.Student;
import org.mapstruct.*;

@Mapper
public interface StudentMapper {

   @Mapping(target = "groupId", expression = "java(student.getGroup().getId())")
   abstract public StudentDto studentToDto(Student student);

   abstract public Student dtoToStudent(StudentDto studentDto);

   @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
   abstract public Student updateStudent(Student studentAfterChanges, @MappingTarget Student studentBeforeChanges);


}