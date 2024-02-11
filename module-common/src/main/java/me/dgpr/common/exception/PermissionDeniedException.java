package me.dgpr.common.exception;

public class PermissionDeniedException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "권한이 없습니다.";

    public PermissionDeniedException() {
        super(MESSAGE_FORMAT);
    }
}
