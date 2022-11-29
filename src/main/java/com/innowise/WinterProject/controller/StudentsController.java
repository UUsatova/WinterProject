package com.innowise.WinterProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/students")
public class StudentsController {

    @GetMapping
    public String getStudents() { //List<Student>

        return "tipa smat studentov";
    }

    ///как доставать студента? По id по имени полному не полному,
    @GetMapping(value = "/{id}")
    public String getStudentById(@PathVariable("id") String id){

        return "";
    }

    public String getStudentByName(){
        return "";
    }
    ////

    public void addStudent(){

    }

    public void editStudent(){}



}
