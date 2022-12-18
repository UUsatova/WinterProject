package com.innowise.WinterProject.controller;

import com.innowise.WinterProject.dto.RoomDto;
import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import com.innowise.WinterProject.mapper.RoomMapper;
import com.innowise.WinterProject.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/rooms")
public class RoomController {
    private final RoomService roomService;
    private final RoomMapper roomMapper;

    @GetMapping
    public ResponseEntity<List<RoomDto>> getRoom() {
        return ResponseEntity.ok(roomService.getAllRooms()
                .stream().map(roomMapper::roomToDto)
                .toList());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable UUID id) {
        return ResponseEntity.ok(roomMapper.roomToDto(roomService.getRoomById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoomDto> addRoom(@RequestBody @Validated(Creation.class) RoomDto roomDto) {//
        return ResponseEntity.ok(roomMapper.roomToDto(
                roomService.addRoom(roomMapper.dtoToRoom(roomDto))));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> removeRoom(@PathVariable UUID id) {
        roomService.removeRoom(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoomDto> updateRoom(@RequestBody @Validated(Update.class) RoomDto roomDto) {
        return ResponseEntity.ok(roomMapper.roomToDto(
                roomService.updateRoom(roomMapper.dtoToRoom(roomDto))));
    }
}
