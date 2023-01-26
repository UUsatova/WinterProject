package com.innowise.WinterProject.service;

import com.innowise.WinterProject.entity.Student;
import com.innowise.WinterProject.entity.Teacher;
import com.innowise.WinterProject.entity.User;
import com.innowise.WinterProject.exeption.ItemNotFoundException;
import com.innowise.WinterProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getByLogin(String login){
        Optional<User> user = userRepository.findByLogin(login);
        return user.orElseThrow(() -> new ItemNotFoundException(login));
    }

    public User getById(UUID id){
        return userRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id, User.class));
    }

    public User addUser(User user){
        return userRepository.save(user);
    }

    public void removeUser(UUID id) {
        userRepository.deleteById(id);
    }

    public User getUserByStudentId(UUID id) {
        return userRepository.findByStudentId(id)
                .orElseThrow(() -> new ItemNotFoundException(id, Student.class));
    }

    public User getUserByTeacherId(UUID id) {
        return userRepository.findByTeacherId(id)
                .orElseThrow(() -> new ItemNotFoundException(id, Teacher.class));
    }

}
