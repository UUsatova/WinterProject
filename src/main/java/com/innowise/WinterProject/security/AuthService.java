package com.innowise.WinterProject.security;

import com.innowise.WinterProject.entity.User;
import com.innowise.WinterProject.service.UserService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    //пароли захештровпны
    private final UserService userService;
    private final JwtProvider jwtProvider;

    public String login(User userBeforeAuth) throws AuthException { // кидать свое исключние
        final User user = userService.getByLogin(userBeforeAuth.getLogin());
        if (user.getPassword().equals(userBeforeAuth.getPassword())) {
            return jwtProvider.generateAccessToken(user);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

}
