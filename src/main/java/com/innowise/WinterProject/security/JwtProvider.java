package com.innowise.WinterProject.security;


import com.innowise.WinterProject.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
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
                .claim("role", user.getRole().toString())
                .compact();
    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        String exeption;
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtAccessSecret)
                    .build()
                    .parseClaimsJws(accessToken);
            return true;
        } catch (ExpiredJwtException expEx) {
            exeption = "Token expired";
        } catch (UnsupportedJwtException unsEx) {
            exeption = "Unsupported jwt";
        } catch (MalformedJwtException mjEx) {
            exeption = "Malformed jwt";
        } catch (SignatureException sEx) {
            exeption = "Invalid signature";
        } catch (Exception e) {
            exeption = "invalid token";
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
