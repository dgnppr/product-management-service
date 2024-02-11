package me.dgpr.common.exception;

public class AuthenticationException extends RuntimeException {

    private static final String MESSAGE = "인증에 실패했습니다.";

    public AuthenticationException() {
        super(MESSAGE);
    }

}
