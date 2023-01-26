package com.innowise.WinterProject.security;

import com.innowise.WinterProject.entity.User;
import com.innowise.WinterProject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String login(User userBeforeAuth) throws Exception {
        String login = userBeforeAuth.getLogin();
        User user = userService.getByLogin(login);
        if (bCryptPasswordEncoder.matches(userBeforeAuth.getPassword(),user.getPassword())) {
            return jwtProvider.generateAccessToken(user);
        } else {
            throw new Exception("Неправильный пароль");
        }
    }

}
