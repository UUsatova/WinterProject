package com.innowise.WinterProject.controller;

import com.innowise.WinterProject.dto.StudentDto;
import com.innowise.WinterProject.entity.Student;
import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import com.innowise.WinterProject.mapper.StudentMapper;
import com.innowise.WinterProject.service.StudentService;
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
@RequestMapping(value = "/students")
public class StudentsController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;


    @Operation(summary = "Get all students")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "Application/json",
                            schema = @Schema(implementation = StudentDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @GetMapping
    public ResponseEntity<List<StudentDto>> getStudents() {
        return ResponseEntity.ok(studentService.getAllStudents()
                .stream().map(studentMapper::studentToDto)
                .toList());
    }

    @Operation(summary = "Get student by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "Application/json",
                            schema = @Schema(implementation = StudentDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @GetMapping(value = "/{id}")
    public ResponseEntity<StudentDto> getStudentById(@Parameter(description = "students' id",example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") @PathVariable UUID id) {
        return ResponseEntity.ok(studentMapper.studentToDto(studentService.getStudentById(id)));
    }


    @Operation(summary = "Add student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "Application/json",
                            schema = @Schema(implementation = StudentDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentDto> createStudent(@RequestBody @Validated(Creation.class) StudentDto newStudent) {
        return ResponseEntity.ok(studentMapper.studentToDto(studentService.createStudent(newStudent)));
    }

    @Operation(summary = "Delete student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeStudent(@PathVariable UUID id) {
        studentService.removeStudent(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "Application/json",
                            schema = @Schema(implementation = StudentDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentDto> updateStudent(@RequestBody @Validated(Update.class) StudentDto studentDto) {
        Student student = studentMapper.dtoToStudent(studentDto);
        studentService.updateStudent(student);
        return ResponseEntity.ok(studentMapper.studentToDto(
                studentService.updateStudent(student)));
    }
}

