package me.dgpr.persistence.service.category.exception;

public class NotFoundCategoryException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "could not find category [param=%s]";

    public NotFoundCategoryException(final String param) {
        super(String.format(MESSAGE_FORMAT, param));
    }
}
