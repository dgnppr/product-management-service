package me.dgpr.persistence.service.product.exception;

public class NotFoundProductException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "could not find product [param=%s]";

    public NotFoundProductException(final String param) {
        super(String.format(MESSAGE_FORMAT, param));
    }
}
