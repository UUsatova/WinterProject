package com.innowise.WinterProject.exeption;

import java.util.UUID;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(UUID id,Class clazz) {
        super(String.format("Can find %s with id: %s", clazz,id ));
    }

    public ItemNotFoundException(String arg) {
        super("Can't find user with such login: " + arg);
    }
}
