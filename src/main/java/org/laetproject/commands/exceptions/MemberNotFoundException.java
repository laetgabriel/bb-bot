package org.laetproject.commands.exceptions;

public class MemberNotFoundException extends RuntimeException{

    public MemberNotFoundException(String message) {
        super(message);
    }
}
