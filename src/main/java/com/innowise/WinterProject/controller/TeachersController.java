package com.innowise.WinterProject.controller;

import com.innowise.WinterProject.dto.TeacherDto;
import com.innowise.WinterProject.entity.Teacher;
import com.innowise.WinterProject.entity.User;
import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import com.innowise.WinterProject.mapper.TeacherMapper;
import com.innowise.WinterProject.mapper.UserMapper;
import com.innowise.WinterProject.service.TeacherService;
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
@RequestMapping(value = "/teachers")
public class TeachersController {

    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;
    private  final UserMapper userAuthMapper;

    @Operation(summary = "Get all teachers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "Application/json",
                            schema = @Schema(implementation = TeacherDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @GetMapping
    ResponseEntity<List<TeacherDto>> getTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers()
                .stream().map(teacherMapper::teacherToDto)
                .toList());
    }

    @Operation(summary = "Get all teacher by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "Application/json",
                            schema = @Schema(implementation = TeacherDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @GetMapping(value = "/{id}")
    ResponseEntity<TeacherDto> getTeacherById(@Parameter(description = "teachars' id",example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") @PathVariable UUID id) {
        return ResponseEntity.ok(teacherMapper.teacherToDto(teacherService.getTeacherById(id)));
    }

    @Operation(summary = "Add teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "Application/json",
                            schema = @Schema(implementation = TeacherDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<TeacherDto> addTeacher(@RequestBody @Validated(Creation.class) TeacherDto teacherDto) {
        Teacher teacher = teacherMapper.dtoToTeacher(teacherDto);
        User user = userAuthMapper.dtoToUser(teacherDto.getUserDto());
        return ResponseEntity.ok(teacherMapper.teacherToDto(
                teacherService.createTeacher(teacherMapper.dtoToTeacher(teacherDto),userAuthMapper.dtoToUser(teacherDto.getUserDto()))));
    }

    @Operation(summary = "Delete teacher by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<?> removeTeacher(@Parameter(description = "teachars' id",example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") @PathVariable UUID id) {
        teacherService.removeTeacher(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "Application/json",
                            schema = @Schema(implementation = TeacherDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<TeacherDto> updateTeacher(@RequestBody @Validated(Update.class) TeacherDto teacherDto) {
        return ResponseEntity.ok(teacherMapper.teacherToDto(
                teacherService.updateTeacher(teacherMapper.dtoToTeacher(teacherDto))));
    }

}
