package me.dgpr.config.exception;

public class PermissionDeniedException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "Operation denied: [cause=%s]";

    public PermissionDeniedException(String cause) {
        super(String.format(MESSAGE_FORMAT, cause));
    }
}
