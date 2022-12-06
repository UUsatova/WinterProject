package com.innowise.WinterProject.servise;

import com.innowise.WinterProject.entity.Student;
import com.innowise.WinterProject.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(UUID id) {
        return studentRepository.findById(id);
    }


    public Student addStudent(Student newStudent) {
        return studentRepository.save(newStudent);
    }

    public boolean removeStudent(UUID id) {
        studentRepository.deleteById(id);
        return getStudentById(id) == null;

    }
//получается тут метод апдейта не нужен совсем????
//    public StudentDto updateStudent(Student student){
//        Student student = getStudentById(studentDto.getId());
//        studentMapper.updateStudent(studentDto);
//        return null;
//    }

}
