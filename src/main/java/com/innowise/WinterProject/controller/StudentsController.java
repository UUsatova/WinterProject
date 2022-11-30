package com.innowise.WinterProject.controller;

import com.innowise.WinterProject.entity.Student;
import com.innowise.WinterProject.servise.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value = "/students")
public class StudentsController {

    StudentService studentService;

    @Autowired
    public StudentsController(StudentService studentService){
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping(value = "/{id}")
    public Student getStudentById(@PathVariable("id") UUID id){
        return studentService.getStudentById(id);
    }

    @PostMapping
    public void addStudent(@RequestBody Student student){
        studentService.addStudent(student);
    }

    @DeleteMapping(value = "/{id}")
    public void removeStudent(@PathVariable("id") UUID id){
        studentService.removeStudent(id);
    }



}
