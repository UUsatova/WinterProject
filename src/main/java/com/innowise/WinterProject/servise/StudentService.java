package com.innowise.WinterProject.servise;

import com.innowise.WinterProject.entity.Student;
import com.innowise.WinterProject.exeption.WrongIdException;
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
    private final GroupService groupService;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(UUID id) {
        return studentRepository.findById(id).orElseThrow(() -> new WrongIdException(id));
    }


    public Student addStudent(Student newStudent) { //вероятно надо увеличить колличество людей в группе
        groupService.increaseNumberOfStudentsInGroup(newStudent.getGroupId());
        return studentRepository.save(newStudent);
    }

    public void removeStudent(UUID id) { //ероятно надо уменьшить колличество людей в группе
        groupService.increaseNumberOfStudentsInGroup(getStudentById(id).getGroupId());
        studentRepository.deleteById(id);
    }

    public Student updateStudent(Student studentAfterChanges) {
        //если меняется номер группы то где-то надо отнять,
        // а где-то прибавить
        Student studentBeforeChanges = getStudentById(studentAfterChanges.getId());
        int groupNumberBeforeChanges = groupService.getGroupById(studentBeforeChanges.getGroupId()).getNumber();
        int groupNumberAfterChanges = groupService.getGroupById(studentAfterChanges.getGroupId()).getNumber();

        if(groupNumberBeforeChanges != groupNumberAfterChanges){
            groupService.decreaseNumberOfStudentsInGroup(studentBeforeChanges.getGroupId());
            groupService.increaseNumberOfStudentsInGroup(studentAfterChanges.getGroupId());
        }

        return studentRepository.save(studentMapper.updateStudent(studentBeforeChanges, studentAfterChanges));
    }



}
