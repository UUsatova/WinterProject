package com.innowise.WinterProject.mapper;

import com.innowise.WinterProject.dto.ScheduleDto;
import com.innowise.WinterProject.entity.Schedule;
import org.mapstruct.*;

@Mapper
public interface ScheduleMapper {


    Schedule dtoToSchedule(ScheduleDto scheduleDto);

    @Mapping(target = "groupId", expression = "java(schedule.getGroup().getId())")
    @Mapping(target = "roomId", expression = "java(schedule.getRoom().getId())")
    @Mapping(target = "teacherId", expression = "java(schedule.getTeacher().getId())")
    @Mapping(target = "disciplineId", expression = "java(schedule.getDiscipline().getId())")
    ScheduleDto scheduleToDto(Schedule schedule);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Schedule updateSchedule(Schedule scheduleAfterChanges, @MappingTarget Schedule scheduleBeforeChanges);

}
