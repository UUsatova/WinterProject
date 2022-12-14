package com.innowise.WinterProject.servise;

import com.innowise.WinterProject.entity.Teacher;
import com.innowise.WinterProject.exeption.WrongIdException;
import com.innowise.WinterProject.mapper.TeacherMapper;
import com.innowise.WinterProject.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherService {
    public final TeacherRepository teacherRepository;
    public final TeacherMapper teacherMapper;


    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(UUID id) {
        return teacherRepository.findById(id).orElseThrow(() -> new WrongIdException(id));
    }

    public Teacher addTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public void removeTeacher(UUID id) {
        teacherRepository.deleteById(id);
    }

    public Teacher updateTeacher(Teacher teacherAfterChanges) {
        return teacherRepository.save(teacherMapper.updateTeacher(
                getTeacherById(teacherAfterChanges.getId()), teacherAfterChanges));
    }

}
