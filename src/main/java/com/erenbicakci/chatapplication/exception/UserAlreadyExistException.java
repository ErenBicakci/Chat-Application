package com.erenbicakci.chatapplication.exception;

import java.io.Serial;

public class UserAlreadyExistException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
