package me.dgpr.api;

import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import me.dgpr.common.exception.AuthenticationException;
import me.dgpr.common.exception.DuplicatedException;
import me.dgpr.common.exception.NotFoundException;
import me.dgpr.common.exception.PermissionDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class CustomRestControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(CustomRestControllerAdvice.class);

    /**
     * 400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    ApiResponse<Void> handleException(ConstraintViolationException e) {
        log.error("", e);
        return ApiResponse.badRequest(e.getMessage());
    }

    /**
     * 400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ApiResponse<Void> handleException(MethodArgumentNotValidException e) {
        log.error("", e);
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" 그리고 "));
        return ApiResponse.badRequest(errorMessage);
    }

    /**
     * 400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    ApiResponse<Void> handleException(MissingRequestHeaderException e) {
        log.error("", e);
        return ApiResponse.badRequest(e.getMessage());
    }

    /**
     * 400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicatedException.class)
    ApiResponse<Void> handleException(DuplicatedException e) {
        log.error("{}", e.getLogMessage(), e);
        return ApiResponse.badRequest(e.getMessage());
    }

    /**
     * 401
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    ApiResponse<Void> handleException(AuthenticationException e) {
        log.error("", e);
        return ApiResponse.unauthorized(e.getMessage());
    }

    /**
     * 403
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(PermissionDeniedException.class)
    ApiResponse<Void> handleException(PermissionDeniedException e) {
        log.error("{}", e.getLogMessage(), e);
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
        log.error("{}", e.getLogMessage(), e);
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
