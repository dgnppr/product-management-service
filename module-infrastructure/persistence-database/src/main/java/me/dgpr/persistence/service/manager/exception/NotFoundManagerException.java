package me.dgpr.persistence.service.manager.exception;

public class NotFoundManagerException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "could not found manager [param=%s]";

    public NotFoundManagerException(final String param) {
        super(String.format(MESSAGE_FORMAT, param));
    }
}
