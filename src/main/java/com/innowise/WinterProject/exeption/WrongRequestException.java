package com.innowise.WinterProject.exeption;

import java.util.UUID;

public class WrongRequestException extends RuntimeException {
    public WrongRequestException(UUID id) {
        super("Wrong id " + id);
    }

    public WrongRequestException(String arg) {
        super(arg);
    }
}
