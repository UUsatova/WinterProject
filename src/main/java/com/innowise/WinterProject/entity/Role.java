package com.innowise.WinterProject.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role  {

    STUDENT("ROLE_STUDENT"),
    TEACHER("ROLE_TEACHER"),
    ADMIN("ROLE_ADMIN");

    private final String value;

    public String getName() {
        return value;
    }

}