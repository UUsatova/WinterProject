package com.innowise.WinterProject.servise;

import com.innowise.WinterProject.entity.Group;
import com.innowise.WinterProject.entity.Student;
import com.innowise.WinterProject.exeption.WrongIdException;
import com.innowise.WinterProject.mapper.StudentMapper;
import com.innowise.WinterProject.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final GroupService groupService;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(UUID id) {
        return studentRepository.findById(id).orElseThrow(() -> new WrongIdException(id));
    }


    public Student addStudent(Student newStudent) {
        groupService.increaseNumberOfStudentsInGroup(newStudent.getGroup());
        return studentRepository.save(newStudent);
    }


    public void removeStudent(UUID id) {
        groupService.decreaseNumberOfStudentsInGroup(getStudentById(id).getGroup());
        studentRepository.deleteById(id);
    }


    public Student updateStudent(Student studentAfterChanges) {
        Student studentBeforeChanges = getStudentById(studentAfterChanges.getId());
        Group groupBeforeChanges = studentBeforeChanges.getGroup();
        Group groupAfterChanges = studentAfterChanges.getGroup();

        if (groupBeforeChanges.equals(groupAfterChanges)) {
            groupService.decreaseNumberOfStudentsInGroup(groupBeforeChanges);
            groupService.increaseNumberOfStudentsInGroup(groupAfterChanges);
        }

        return studentRepository.save(studentMapper.updateStudent(studentBeforeChanges, studentAfterChanges));
    }



}
