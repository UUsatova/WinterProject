package com.innowise.WinterProject.service;

import com.innowise.WinterProject.dto.StudentDto;
import com.innowise.WinterProject.entity.Group;
import com.innowise.WinterProject.entity.Student;
import com.innowise.WinterProject.entity.User;
import com.innowise.WinterProject.exeption.WrongIdException;
import com.innowise.WinterProject.mapper.StudentMapper;
import com.innowise.WinterProject.mapper.UserMapper;
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
    private final UserService userService;

    private final UserMapper userMapper;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(UUID id) {
        return studentRepository.findById(id).orElseThrow(() -> new WrongIdException(id));
    }


    public Student createStudent(StudentDto newStudent) {
        User user = userMapper.dtoToUser(newStudent.getUserDto());
        Group group = groupService.getGroupById( newStudent.getGroupId());
        Student student = studentMapper.dtoToStudent(newStudent);
        student.setGroup(group);
        Student addedStudent = addStudent(student);
        user.setStudentId(addedStudent.getId());
        userService.addUser(user);
        return addedStudent;
    }

    private Student addStudent(Student newStudent){
        groupService.increaseNumberOfStudentsInGroup(newStudent.getGroup());
        return studentRepository.save(newStudent);
    }

    public void removeStudent(UUID id) {
        groupService.decreaseNumberOfStudentsInGroup(getStudentById(id).getGroup());

        //возможно можно проще
        userService.removeUser(userService.getUserByStudentId(id).getId());

        studentRepository.deleteById(id);
    }


    public Student updateStudent(Student studentAfterChanges) {
        Student studentBeforeChanges = getStudentById(studentAfterChanges.getId());

        if(studentAfterChanges.getGroup()!=null){
            Group groupBeforeChanges = studentBeforeChanges.getGroup();
            Group groupAfterChanges = studentAfterChanges.getGroup();
            groupService.decreaseNumberOfStudentsInGroup(groupBeforeChanges);
            groupService.increaseNumberOfStudentsInGroup(groupAfterChanges);

        }

        return studentRepository.save(studentMapper.updateStudent(studentAfterChanges,studentBeforeChanges));
    }


}
