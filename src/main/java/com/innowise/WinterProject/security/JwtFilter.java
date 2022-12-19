package com.innowise.WinterProject.security;

import com.innowise.WinterProject.entity.Role;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private static final String AUTHORIZATION = "Authorization";
    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
            throws IOException, ServletException {
        final String token = getTokenFromRequest((HttpServletRequest) request);
        if (token != null && jwtProvider.validateAccessToken(token)) {
            final Claims claims = jwtProvider.getAccessClaims(token);

            final JwtAuthentication jwtInfoToken = generate(claims);
            jwtInfoToken.setAuthenticated(true);

            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
        }
        fc.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setLogin(claims.get("login", String.class));
        return jwtInfoToken;
    }

    private static Set<Role> getRoles(Claims claims) {
        Set<Role> roles = new HashSet<>(Collections.singleton(Role.valueOf(claims.get("role", String.class))));
        return roles;
    }
}
