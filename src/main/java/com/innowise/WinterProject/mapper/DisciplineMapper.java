package com.innowise.WinterProject.mapper;

import com.innowise.WinterProject.dto.DisciplineDto;
import com.innowise.WinterProject.dto.StudentDto;
import com.innowise.WinterProject.entity.Discipline;
import com.innowise.WinterProject.entity.Student;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface DisciplineMapper {
    DisciplineDto disciplineToDto(Discipline discipline);
    Discipline dtoToDiscipline(DisciplineDto disciplineDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Discipline updateDiscipline(Discipline disciplineBeforeChanges, @MappingTarget Discipline disciplineAfterChanges);
}
