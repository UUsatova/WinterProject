package com.innowise.WinterProject.service;

import com.innowise.WinterProject.entity.User;
import com.innowise.WinterProject.exeption.WrongIdException;
import com.innowise.WinterProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public User getByLogin(String login){//делать такое исключение ? Уж больно сильно оно похоже на то что с айдишником
        return userRepository.findByLogin(login).orElseThrow(() -> new WrongLoginException(login));
    }

    public User getById(UUID id){
        return userRepository.findById(id).orElseThrow(() -> new WrongIdException(id));
    }

    public User addUser(User user){
        return userRepository.save(user);
    }

    public void removeUser(UUID id) {
        userRepository.deleteById(id);
    }

}
