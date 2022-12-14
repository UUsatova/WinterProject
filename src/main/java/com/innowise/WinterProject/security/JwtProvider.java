package com.innowise.WinterProject.security;


import com.innowise.WinterProject.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey jwtAccessSecret;

    public JwtProvider(@Value("${jwt.secret.access}") String jwtAccessSecret) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(jwtAccessSecret.getBytes());
    }

    public String generateAccessToken(User user) {
        return Jwts.builder().setSubject(user.getLogin())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(jwtAccessSecret)
                .claim("login", user.getLogin())
                .claim("role", user.getRole())
                .compact();
    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtAccessSecret)
                    .build()
                    .parseClaimsJws(accessToken);
            return true;
        } catch (Exception e) {
            //тут кстати было логирование тысячи и одного эксепшена мне тоже так сделать?
        }
        return false;
    }


    public Claims getAccessClaims(@NonNull String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtAccessSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
