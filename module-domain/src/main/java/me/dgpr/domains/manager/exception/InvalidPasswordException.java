package me.dgpr.domains.manager.exception;

public class InvalidPasswordException extends RuntimeException {
    
    private static final String MESSAGE_FORMAT = "Invalid password [param=%s]";

    public InvalidPasswordException(String param) {
        super(String.format(MESSAGE_FORMAT, param));
    }
}
