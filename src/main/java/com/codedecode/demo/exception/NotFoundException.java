package com.codedecode.demo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class NotFoundException extends RrsException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
