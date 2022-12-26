package com.innowise.WinterProject.controller;

import com.innowise.WinterProject.dto.RoomDto;
import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import com.innowise.WinterProject.mapper.RoomMapper;
import com.innowise.WinterProject.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all rooms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "Application/json",
                            schema = @Schema(implementation = RoomDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @GetMapping
    public ResponseEntity<List<RoomDto>> getRoom() {
        return ResponseEntity.ok(roomService.getAllRooms()
                .stream().map(roomMapper::roomToDto)
                .toList());
    }

    @Operation(summary = "Get room by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "Application/json",
                            schema = @Schema(implementation = RoomDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @GetMapping(value = "/{id}")
    public ResponseEntity<RoomDto> getRoomById(@Parameter(description = "rooms' id",example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") @PathVariable UUID id) {
        return ResponseEntity.ok(roomMapper.roomToDto(roomService.getRoomById(id)));
    }

    @Operation(summary = "Add room ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "Application/json",
                            schema = @Schema(implementation = RoomDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomDto> addRoom( @RequestBody @Validated(Creation.class) RoomDto roomDto) {//
        return ResponseEntity.ok(roomMapper.roomToDto(
                roomService.addRoom(roomMapper.dtoToRoom(roomDto))));
    }

    @Operation(summary = "Delete room ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeRoom(@PathVariable UUID id) {
        roomService.removeRoom(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update room ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "Application/json",
                            schema = @Schema(implementation = RoomDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomDto> updateRoom(@RequestBody @Validated(Update.class) RoomDto roomDto) {
        return ResponseEntity.ok(roomMapper.roomToDto(
                roomService.updateRoom(roomMapper.dtoToRoom(roomDto))));
    }
}
