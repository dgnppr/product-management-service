package me.dgpr.domains.manager.exception;

public class DuplicatedManagerException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "Duplicated manager [param=%s]";

    public DuplicatedManagerException(final String param) {
        super(String.format(MESSAGE_FORMAT, param));
    }

}
