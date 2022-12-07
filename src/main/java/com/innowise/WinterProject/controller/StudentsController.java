package com.innowise.WinterProject.controller;

import com.innowise.WinterProject.dto.StudentDto;
import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.group.Update;
import com.innowise.WinterProject.mapper.StudentMapper;
import com.innowise.WinterProject.servise.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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


    @GetMapping
    public ResponseEntity<List<StudentDto>> getStudents() {
        List<StudentDto> allStudents = studentService.getAllStudents()
                .stream().map(studentMapper::studentToDto)
                .toList();
        return ResponseEntity.ok(allStudents);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable UUID id) {
        return ResponseEntity.ok(studentMapper.studentToDto(studentService.getStudentById(id)));
    }

    @PostMapping
    public ResponseEntity<StudentDto> addStudent(@RequestBody @Validated(Creation.class) StudentDto newStudent) {
        return ResponseEntity.ok(studentMapper.studentToDto(
                studentService.addStudent(studentMapper.dtoToStudent(newStudent))));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> removeStudent(@PathVariable UUID id) {
        studentService.removeStudent(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<StudentDto> updateStudent(@RequestBody @Validated(Update.class) StudentDto studentDto) {
        return ResponseEntity.ok(studentMapper.studentToDto(
                studentService.updateStudent(studentMapper.dtoToStudent(studentDto))));
    }
}

