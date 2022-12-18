package com.innowise.WinterProject.mapper;

import com.innowise.WinterProject.dto.ScheduleDto;
import com.innowise.WinterProject.entity.Schedule;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface ScheduleMapper {

    Schedule dtoToSchedule(ScheduleDto scheduleDto);
    ScheduleDto scheduleToDto(Schedule schedule);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Schedule updateSchedule(Schedule scheduleAfterChanges , @MappingTarget Schedule scheduleBeforeChanges);

}
