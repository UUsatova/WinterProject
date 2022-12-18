package com.innowise.WinterProject.controller;

import com.innowise.WinterProject.dto.TeacherDto;
import com.innowise.WinterProject.entity.Teacher;
import com.innowise.WinterProject.entity.User;
import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import com.innowise.WinterProject.mapper.TeacherMapper;
import com.innowise.WinterProject.mapper.UserMapper;
import com.innowise.WinterProject.service.TeacherService;
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

    @GetMapping
    ResponseEntity<List<TeacherDto>> getTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers()
                .stream().map(teacherMapper::teacherToDto)
                .toList());
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<TeacherDto> getTeacherById(@PathVariable UUID id) {
        return ResponseEntity.ok(teacherMapper.teacherToDto(teacherService.getTeacherById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<TeacherDto> addTeacher(@RequestBody @Validated(Creation.class) TeacherDto teacherDto) {
        Teacher teacher = teacherMapper.dtoToTeacher(teacherDto);
        User user = userAuthMapper.dtoToUser(teacherDto.getUserDto());
        return ResponseEntity.ok(teacherMapper.teacherToDto(
                teacherService.createTeacher(teacherMapper.dtoToTeacher(teacherDto),userAuthMapper.dtoToUser(teacherDto.getUserDto()))));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<?> removeTeacher(@PathVariable UUID id) {
        teacherService.removeTeacher(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<TeacherDto> updateTeacher(@RequestBody @Validated(Update.class) TeacherDto teacherDto) {
        return ResponseEntity.ok(teacherMapper.teacherToDto(
                teacherService.updateTeacher(teacherMapper.dtoToTeacher(teacherDto))));
    }

}
