package com.example.board.controller;

import com.example.board.dto.ApiResponse;
import com.example.board.dto.UserLoginRequestDto;
import com.example.board.dto.UserRequestDto;
import com.example.board.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> save(
            @Valid @RequestBody UserRequestDto requestDto) {

        userService.save(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(
            @Valid @RequestBody UserLoginRequestDto requestDto) {

        userService.login(requestDto);

        return ResponseEntity.ok(ApiResponse.success());
    }
}