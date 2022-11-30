package com.innowise.WinterProject.servise;

import com.innowise.WinterProject.entity.Student;
import com.innowise.WinterProject.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StudentService {

    StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository){
        this.studentRepository=studentRepository;
    }

    public Student getStudentById(UUID id){
        return  studentRepository.getReferenceById(id);
    }

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    public void addStudent(Student student){
        studentRepository.save(student);
    }

    public void removeStudent(UUID id){
        studentRepository.deleteById(id);

    }
    //дописать по мере необходимости

}
