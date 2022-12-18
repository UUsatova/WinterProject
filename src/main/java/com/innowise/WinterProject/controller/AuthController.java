package com.innowise.WinterProject.controller;

import com.innowise.WinterProject.dto.UserDto;
import com.innowise.WinterProject.entity.User;
import com.innowise.WinterProject.mapper.UserMapper;
import com.innowise.WinterProject.security.AuthService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserMapper userAuthMapper; //мультипартформ

    //все пользователи
    @PostMapping
    public ResponseEntity<String> login(@RequestBody UserDto userAuthDto) throws AuthException { //убрать
        User user =  userAuthMapper.dtoToUser(userAuthDto);
        String token = authService.login(user);
        return ResponseEntity.ok(token);
    }

}
