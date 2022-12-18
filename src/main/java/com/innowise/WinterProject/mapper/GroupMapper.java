package com.innowise.WinterProject.mapper;

import com.innowise.WinterProject.dto.GroupDto;
import com.innowise.WinterProject.entity.Group;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface GroupMapper {
    Group dtoToGroup(GroupDto groupDto);

    GroupDto groupToDto(Group groupDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Group updateGroup(Group groupAfterChanges,@MappingTarget Group groupBeforeChanges);

}
