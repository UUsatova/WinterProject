package com.innowise.WinterProject.controller;

import com.innowise.WinterProject.dto.StudentDto;
import com.innowise.WinterProject.entity.Student;
import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import com.innowise.WinterProject.mapper.StudentMapper;
import com.innowise.WinterProject.mapper.UserMapper;
import com.innowise.WinterProject.service.StudentService;
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
    private  final UserMapper userAuthMapper;


    @GetMapping
    public ResponseEntity<List<StudentDto>> getStudents() {
        return ResponseEntity.ok(studentService.getAllStudents()
                .stream().map(studentMapper::studentToDto)
                .toList());
    }

    //сделать норм возврат
    @GetMapping(value = "/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable UUID id) {
        return ResponseEntity.ok(studentMapper.studentToDto(studentService.getStudentById(id)));
    }


    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<StudentDto> createStudent(@RequestBody @Validated(Creation.class) StudentDto newStudent) {
        return ResponseEntity.ok(studentMapper.studentToDto(studentService.createStudent(newStudent)));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> removeStudent(@PathVariable UUID id) {
        studentService.removeStudent(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<StudentDto> updateStudent(@RequestBody @Validated(Update.class) StudentDto studentDto) {
        Student student = studentMapper.dtoToStudent(studentDto);
        studentService.updateStudent(student);
        return ResponseEntity.ok(studentMapper.studentToDto(
                studentService.updateStudent(student)));
    }
}

