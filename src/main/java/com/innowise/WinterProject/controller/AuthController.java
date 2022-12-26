package com.innowise.WinterProject.controller;

import com.innowise.WinterProject.dto.UserDto;
import com.innowise.WinterProject.entity.User;
import com.innowise.WinterProject.mapper.UserMapper;
import com.innowise.WinterProject.security.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class AuthController {
    private final AuthService authService;
    private final UserMapper userAuthMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Operation(summary = "Enter your credentials (login,password and role)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",content =
                    { @Content(mediaType = "text/plain",
                    schema = @Schema(defaultValue = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")) }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody UserDto userAuthDto) throws Exception {
        User user =  userAuthMapper.dtoToUser(userAuthDto);
        String token = authService.login(user);
        return ResponseEntity.ok(token);
    }

}
