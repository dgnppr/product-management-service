package me.dgpr.persistence.service.store.exception;

public class NotFoundStoreException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "could not find store [param=%s]";

    public NotFoundStoreException(final String param) {
        super(String.format(MESSAGE_FORMAT, param));
    }
}
