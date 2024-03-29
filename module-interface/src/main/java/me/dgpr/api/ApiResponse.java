package me.dgpr.api;

import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
        Meta meta,
        T data
) {

    public static <T> ApiResponse<T> of(
            int code,
            String message,
            T data
    ) {
        return new ApiResponse<>(
                new Meta(code, message),
                data
        );
    }

    public static <T> ApiResponse<T> ok() {
        return of(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                null
        );
    }

    public static <T> ApiResponse<T> ok(T data) {
        return of(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                data
        );
    }

    public static <T> ApiResponse<T> created() {
        return of(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                null
        );
    }

    public static <T> ApiResponse<T> created(T data) {
        return of(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                data
        );
    }

    public static <T> ApiResponse<T> badRequest(String errMessage) {
        return of(
                HttpStatus.BAD_REQUEST.value(),
                errMessage,
                null
        );
    }

    public static <T> ApiResponse<T> notFound(String errMessage) {
        return of(
                HttpStatus.NOT_FOUND.value(),
                errMessage,
                null
        );
    }

    public static <T> ApiResponse<T> serverError() {
        return of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                null
        );
    }

    public static ApiResponse<Void> forbidden(final String message) {
        return of(
                HttpStatus.FORBIDDEN.value(),
                message,
                null
        );
    }

    public static ApiResponse<Void> unauthorized(final String message) {
        return of(
                HttpStatus.UNAUTHORIZED.value(),
                message,
                null
        );
    }

    public record Meta(
            int code,
            String message
    ) {

    }
}
