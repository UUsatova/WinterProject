package com.innowise.WinterProject.exeption;

import java.util.UUID;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(UUID id) {
        super("Wrong id " + id);
    }

    public ItemNotFoundException(String arg) {
        super(arg);
    }
}
