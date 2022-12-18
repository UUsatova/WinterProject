package com.innowise.WinterProject.mapper;

import com.innowise.WinterProject.dto.DisciplineDto;
import com.innowise.WinterProject.entity.Discipline;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface DisciplineMapper {
    DisciplineDto disciplineToDto(Discipline discipline);
    Discipline dtoToDiscipline(DisciplineDto disciplineDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Discipline updateDiscipline(Discipline disciplineAfterChanges, @MappingTarget Discipline disciplineBeforeChanges);
}
