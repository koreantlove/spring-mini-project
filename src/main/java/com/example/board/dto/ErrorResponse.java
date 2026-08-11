package com.example.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private boolean success;
    private String message;

    private Map<String, String> errors;

    public static ErrorResponse error(String message) {
        return new ErrorResponse(
                false,
                message,
                null
        );
    }

    public static ErrorResponse validation(
            String message,
            Map<String, String> errors) {

        return new ErrorResponse(
                false,
                message,
                errors
        );
    }
}