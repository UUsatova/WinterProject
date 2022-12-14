package com.innowise.WinterProject.service;

import com.innowise.WinterProject.entity.Role;
import com.innowise.WinterProject.entity.Teacher;
import com.innowise.WinterProject.entity.User;
import com.innowise.WinterProject.exeption.WrongIdException;
import com.innowise.WinterProject.mapper.TeacherMapper;
import com.innowise.WinterProject.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherService extends UserService {
    public final TeacherRepository teacherRepository;

    private final UserService userService;
    public final TeacherMapper teacherMapper;


    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(UUID id) {
        return teacherRepository.findById(id).orElseThrow(() -> new WrongIdException(id));
    }

    public Teacher addTeacher(Teacher teacher) {
        User user = new User();
        user.setRole(Role.STUDENT);
        user.setLogin(teacher.getLogin());
        user.setPassword(teacher.getPassword());
        userService.addUser(user);

        teacher.setId(user.getId());
        return teacherRepository.save(teacher);
    }

    public void removeTeacher(UUID id) {
        userService.removeUser(id);
        teacherRepository.deleteById(id);
    }

    public Teacher updateTeacher(Teacher teacherAfterChanges) {
        return teacherRepository.save(teacherMapper.updateTeacher(
                getTeacherById(teacherAfterChanges.getId()), teacherAfterChanges));
    }

}
