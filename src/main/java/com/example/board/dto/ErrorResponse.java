package com.example.board.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ErrorResponse {

    private final int status;
    private final String error;
    private final String message;
    private final LocalDateTime timestamp;
    private final Map<String, String> validationErrors;

    private ErrorResponse(
            int status,
            String error,
            String message,
            Map<String, String> validationErrors
    ) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.validationErrors = validationErrors;
        this.timestamp = LocalDateTime.now();
    }

    public static ErrorResponse error(
            int status,
            String error,
            String message
    ) {
        return new ErrorResponse(
                status,
                error,
                message,
                null
        );
    }

    public static ErrorResponse validation(
            String message,
            Map<String, String> validationErrors
    ) {
        return new ErrorResponse(
                400,
                "VALIDATION_ERROR",
                message,
                validationErrors
        );
    }
}