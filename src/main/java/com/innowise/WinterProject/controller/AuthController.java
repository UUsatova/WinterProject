package com.innowise.WinterProject.controller;

import com.innowise.WinterProject.dto.UserAuthDto;
import com.innowise.WinterProject.group.Creation;
import com.innowise.WinterProject.mapper.UserAuthMapper;
import com.innowise.WinterProject.security.AuthService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserAuthMapper userAuthMapper; //мультипартформ
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Validated(Creation.class) UserAuthDto userAuthDto) throws AuthException { //убрать
        return ResponseEntity.ok(authService.login(userAuthMapper.dtoToUser(userAuthDto)));
    }

}
