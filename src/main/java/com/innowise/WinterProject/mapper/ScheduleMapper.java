package com.innowise.WinterProject.mapper;

import com.innowise.WinterProject.dto.ScheduleDto;
import com.innowise.WinterProject.entity.Schedule;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface ScheduleMapper {


    Schedule dtoToSchedule(ScheduleDto scheduleDto);

    @Mapping(target = "groupId", expression = "java(schedule.getGroup().getId())")
    @Mapping(target = "roomId", expression = "java(schedule.getRoom().getId())")
    @Mapping(target = "teacherId", expression = "java(schedule.getTeacher().getId())")
    @Mapping(target = "disciplineId", expression = "java(schedule.getDiscipline().getId())")
    ScheduleDto scheduleToDto(Schedule schedule);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Schedule updateSchedule(Schedule source, @MappingTarget Schedule target);

}
