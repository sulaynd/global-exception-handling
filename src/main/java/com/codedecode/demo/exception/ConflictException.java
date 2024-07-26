package com.codedecode.demo.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class ConflictException extends RrsException {

    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}

