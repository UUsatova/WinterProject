package com.innowise.WinterProject.mapper;

import com.innowise.WinterProject.dto.RoomDto;
import com.innowise.WinterProject.entity.Room;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
@Mapper
public interface RoomMapper {

    RoomDto roomToDto(Room room);
    Room dtoToRoom(RoomDto roomDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Room updateRoom(Room roomBeforeChanges, @MappingTarget Room roomAfterChanges);

}
