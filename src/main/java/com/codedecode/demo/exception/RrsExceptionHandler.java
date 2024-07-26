package com.codedecode.demo.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.naming.ServiceUnavailableException;
import javax.xml.bind.ValidationException;
import java.time.Instant;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class RrsExceptionHandler {

    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server Error. Please contact support.";

    @ExceptionHandler({
            BadRequestException.class,
            ConstraintViolationException.class,
            //NoResourceFoundException.class,
            //HandlerMethodValidationException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentNotValidException.class,
            MismatchedInputException.class,
            HttpMessageNotReadableException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception exception) {
        return buildErrorResponse(mapToRrsException(exception, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleNotAuthorizedException(UnauthorizedException exception) {
        return buildErrorResponse(exception);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(RrsException exception) {
        //return buildErrorResponse(exception);
        return buildErrorResponse(mapToRrsException(exception, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException exception) {
        return buildErrorResponse(mapToRrsException(exception, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleEmptyResultDataAccessException(EmptyResultDataAccessException exception) {
        return buildErrorResponse(mapToRrsException(exception, HttpStatus.NOT_FOUND));
    }


    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ErrorResponse> handleUnProcessableContentException(ValidationException exception) {
        return buildErrorResponse(mapToRrsException(exception, HttpStatus.UNPROCESSABLE_ENTITY));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException exception) {
        return buildErrorResponse(mapToRrsException(exception, HttpStatus.METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleServerException(Exception exception) {
        return buildErrorResponse(mapToRrsException(exception, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponse> handleServiceUnavailableException(ServiceUnavailableException exception) {
        return buildErrorResponse(mapToRrsException(exception, HttpStatus.SERVICE_UNAVAILABLE));
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleConflictException(RrsException rrsException) {
        return buildErrorResponse(rrsException);
    }
    private RrsException mapToRrsException(Exception e, HttpStatus httpStatus) {
        if (null == httpStatus) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        RrsException rrse = new RrsException(httpStatus, e.getMessage());
        log.error("Exception occurred with responseId: {} errorCode: {} and message: '{}'",
                rrse.getId(),
                rrse.getHttpStatus().toString(),
                e.getMessage()
        );
        e.printStackTrace();
        return rrse;
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(RrsException e) {
        String message = HttpStatus.INTERNAL_SERVER_ERROR == e.getHttpStatus() ?
                INTERNAL_SERVER_ERROR_MESSAGE :
                e.getMessage();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpCode(e.getHttpStatus().value())
                .message(message)
                .errorCode(e.getHttpStatus().toString())
                .responseId(e.getId())
                .timestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorResponse, e.getHttpStatus());
    }
}
