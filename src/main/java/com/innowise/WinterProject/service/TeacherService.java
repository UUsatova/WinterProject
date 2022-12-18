package com.innowise.WinterProject.service;

import com.innowise.WinterProject.entity.Teacher;
import com.innowise.WinterProject.entity.User;
import com.innowise.WinterProject.exeption.WrongIdException;
import com.innowise.WinterProject.mapper.TeacherMapper;
import com.innowise.WinterProject.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TeacherService {
    public final TeacherRepository teacherRepository;

    private final UserService userService;
    public final TeacherMapper teacherMapper;


    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(UUID id) {
        return teacherRepository.findById(id).orElseThrow(() -> new WrongIdException(id));
    }

    public Teacher createTeacher(Teacher teacher, User user) {
        Teacher addedTeacher = addTeacher(teacher);
        user.setTeacherId(addedTeacher.getId());
        userService.addUser(user);
        return addedTeacher;
    }

    private   Teacher addTeacher(Teacher teacher){
        return  teacherRepository.save(teacher);
    }

    public void removeTeacher(UUID id) {
        userService.removeUser(userService.getUserByTeacherId(id).getId());
        teacherRepository.deleteById(id);
    }

    public Teacher updateTeacher(Teacher teacherAfterChanges) {
        return teacherRepository.save(teacherMapper.updateTeacher(
                teacherAfterChanges,getTeacherById(teacherAfterChanges.getId()) ));
    }

}