package com.codedecode.demo.exception;

import org.springframework.http.HttpStatus;

public class MissingArgumentsException extends RrsException {

    public MissingArgumentsException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
