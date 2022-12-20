package com.innowise.WinterProject.controller;

import com.innowise.WinterProject.dto.UserDto;
import com.innowise.WinterProject.entity.User;
import com.innowise.WinterProject.mapper.UserMapper;
import com.innowise.WinterProject.security.AuthService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class AuthController {
    private final AuthService authService;
    private final UserMapper userAuthMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody UserDto userAuthDto) throws AuthException {
        User user =  userAuthMapper.dtoToUser(userAuthDto);
        String token = authService.login(user);
        return ResponseEntity.ok(token);
    }

}
