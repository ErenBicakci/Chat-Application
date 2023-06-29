package com.erenbicakci.chatapplication.exception;

import java.io.Serial;

public class ChatRoomNotFoundException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public ChatRoomNotFoundException(String message) {
        super(message);
    }
}
