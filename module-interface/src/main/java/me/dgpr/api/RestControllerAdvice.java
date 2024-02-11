package me.dgpr.api;

import jakarta.validation.ConstraintViolationException;
import me.dgpr.common.exception.NotFoundException;
import me.dgpr.common.exception.PermissionDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

// TODO logging
@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(RestControllerAdvice.class);

    /**
     * 400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    ApiResponse<Void> handleException(ConstraintViolationException e) {
        return ApiResponse.badRequest(e.getMessage());
    }

    /**
     * 400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ApiResponse<Void> handleException(MethodArgumentNotValidException e) {
        return ApiResponse.badRequest(e.getMessage());
    }

    /**
     * 400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    ApiResponse<Void> handleException(MissingRequestHeaderException e) {
        return ApiResponse.badRequest(e.getMessage());
    }

    /**
     * 403
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(PermissionDeniedException.class)
    ApiResponse<Void> handleException(PermissionDeniedException e) {
        return ApiResponse.forbidden(e.getMessage());
    }

    /**
     * 404
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    ApiResponse<Void> handleException(NoHandlerFoundException e) {
        return ApiResponse.notFound(e.getMessage());
    }

    /**
     * 404
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    ApiResponse<Void> handleException(NotFoundException e) {
        return ApiResponse.notFound(e.getMessage());
    }

    /**
     * 500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    ApiResponse<Void> handleException(Exception e) {
        log.error("", e);
        return ApiResponse.serverError();
    }
}
