package com.innowise.WinterProject.controller;

import com.innowise.WinterProject.dto.RoomDto;
import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import com.innowise.WinterProject.mapper.RoomMapper;
import com.innowise.WinterProject.servise.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<RoomDto>> getStudents() {
        return ResponseEntity.ok(roomService.getAllRooms()
                .stream().map(roomMapper::roomToDto)
                .toList());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<RoomDto> getStudentById(@PathVariable UUID id) {
        return ResponseEntity.ok(roomMapper.roomToDto(roomService.getRoomById(id)));
    }

    @PostMapping
    public ResponseEntity<RoomDto> addStudent(@RequestBody @Validated(Creation.class) RoomDto roomDto) {
        return ResponseEntity.ok(roomMapper.roomToDto(
                roomService.addRoom(roomMapper.dtoToRoom(roomDto))));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> removeStudent(@PathVariable UUID id) {
        roomService.removeRoom(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<RoomDto> updateStudent(@RequestBody @Validated(Update.class) RoomDto roomDto) {
        return ResponseEntity.ok(roomMapper.roomToDto(
                roomService.updateRoom(roomMapper.dtoToRoom(roomDto))));
    }
}
