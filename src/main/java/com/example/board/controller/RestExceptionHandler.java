package com.example.board.controller;

import com.example.board.dto.ApiResponse;
import com.example.board.dto.ErrorResponse;
import com.example.board.exception.BoardNotFoundException;
import com.example.board.exception.InvalidLoginException;
import com.example.board.exception.UserAlreadyExistsException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Order(1)
public class RestExceptionHandler {

    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBoardNotFound(
            BoardNotFoundException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ErrorResponse.error(
                                e.getMessage()
                        )
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        /*ErrorResponse response =
                new ErrorResponse( false,
                        "입력값이 올바르지 않습니다.",
                        errors
                );*/

        return ResponseEntity
                .badRequest()
                .body(
                        ErrorResponse.validation(
                                "입력값이 올바르지 않습니다.",
                                errors
                        )
                );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(
            UserAlreadyExistsException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ErrorResponse.error(
                                e.getMessage()
                        )
                );
    }

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<ErrorResponse> handleInvalidLogin(
            InvalidLoginException e) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ErrorResponse.error(
                                e.getMessage()
                        )
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException e) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ErrorResponse.error(
                                "아이디 또는 비밀번호가 올바르지 않습니다."
                        )
                );
    }
}