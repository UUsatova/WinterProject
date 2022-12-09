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

    @Transactional
    public Student addStudent(Student newStudent) { //вероятно надо увеличить колличество людей в группе
        groupService.increaseNumberOfStudentsInGroup(newStudent.getGroup());
        return studentRepository.save(newStudent);
    }

    @Transactional
    public void removeStudent(UUID id) { //ероятно надо уменьшить колличество людей в группе
        groupService.decreaseNumberOfStudentsInGroup(getStudentById(id).getGroup());
        studentRepository.deleteById(id);
    }

    @Transactional
    public Student updateStudent(Student studentAfterChanges) {
        //если меняется номер группы то где-то надо отнять,
        // а где-то прибавить
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
