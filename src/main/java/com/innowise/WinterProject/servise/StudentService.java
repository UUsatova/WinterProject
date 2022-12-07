package com.innowise.WinterProject.servise;

import com.innowise.WinterProject.entity.Student;
import com.innowise.WinterProject.exeption.WrongRequestException;
import com.innowise.WinterProject.mapper.StudentMapper;
import com.innowise.WinterProject.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(UUID id) {
        return studentRepository.findById(id).orElseThrow(() -> new WrongRequestException(id));
    }


    public Student addStudent(Student newStudent) {
        return studentRepository.save(newStudent);
    }

    public void removeStudent(UUID id) {
        studentRepository.deleteById(id);
    }

    public Student updateStudent(Student studentAfterChanges) {
        Student studentBeforeChanges = studentRepository.findById(studentAfterChanges.getId()).get();
        return studentRepository.save(studentMapper.updateStudent(studentBeforeChanges, studentAfterChanges));
    }

}
