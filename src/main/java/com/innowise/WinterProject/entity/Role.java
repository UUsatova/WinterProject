package com.innowise.WinterProject.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {

    STUDENT("STUDENT"),
    TEACHER("TEACHER");

    private final String value;
    @Override
    public String getAuthority() {
        return value;
    }

}