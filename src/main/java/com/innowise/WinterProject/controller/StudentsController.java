package com.innowise.WinterProject.controller;

import com.innowise.WinterProject.dto.StudentDto;
import com.innowise.WinterProject.entity.Student;
import com.innowise.WinterProject.group.Set;
import com.innowise.WinterProject.mapper.StudentMapper;
import com.innowise.WinterProject.servise.StudentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Validated
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
    public ResponseEntity<StudentDto> getStudentById(@PathVariable @NotNull UUID id) {
        Optional<Student> student = studentService.getStudentById(id);
        return student.map(value -> ResponseEntity.ok(studentMapper.studentToDto(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StudentDto> addStudent(@RequestBody @Validated(Set.class) StudentDto newStudent) {
        StudentDto respStudent = studentMapper.studentToDto(studentService.addStudent(studentMapper.dtoToStudent(newStudent)));
        return respStudent != null ? ResponseEntity.ok(respStudent) : ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> removeStudent(@PathVariable @NotNull UUID id) {
        studentService.removeStudent(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<StudentDto> updateStudent(@RequestBody @Valid StudentDto newStudent) {
        Optional<Student> student = studentService.getStudentById(newStudent.getId());
        return student.map(value -> {
            studentMapper.updateStudent(value, newStudent);
            return ResponseEntity.ok(studentMapper.studentToDto(value));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

//норма ли столько вложенностей или лучше разбить
//метод апдейт только в контроллере
// правильно ли я тут вообще эти опшиналы прописала