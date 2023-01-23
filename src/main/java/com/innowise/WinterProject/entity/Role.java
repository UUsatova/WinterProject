package com.innowise.WinterProject.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role  {

    STUDENT("STUDENT"), //ROLE_
    TEACHER("TEACHER"),
    ADMIN("ADMIN");

    private final String value;

    public String getName() {
        return value;
    }

}