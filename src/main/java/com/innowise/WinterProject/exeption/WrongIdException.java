package com.innowise.WinterProject.exeption;

import java.util.UUID;

public class WrongIdException extends RuntimeException {
    public WrongIdException(UUID id) {
        super("Wrong id " + id);
    }

    public WrongIdException(String arg) {
        super(arg);
    }
}
