package com.codedecode.demo.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
public class ErrorResponse {

    private Integer httpCode;

    private String message;

    private String errorCode;

    private UUID responseId;

    private Instant timestamp;
}

