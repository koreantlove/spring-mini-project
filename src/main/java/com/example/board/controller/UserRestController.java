package com.example.board.controller;

import com.example.board.dto.*;
import com.example.board.security.CustomUserDetails;
import com.example.board.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /*
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(
            @Valid @RequestBody UserLoginRequestDto requestDto) {

        userService.login(requestDto);

        return ResponseEntity.ok(ApiResponse.success());
    }*/

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(
            @Valid @RequestBody UserLoginRequestDto requestDto,
            HttpSession session) {

        //Authentication authentication = userService.login(requestDto);
        String token = userService.login(requestDto);
        JwtResponse response = new JwtResponse(token);

        /*
        SecurityContext securityContext =
                SecurityContextHolder.createEmptyContext();

        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        session.setAttribute(
                HttpSessionSecurityContextRepository
                        .SPRING_SECURITY_CONTEXT_KEY,
                securityContext
        );
        */

        return ResponseEntity
                .ok(ApiResponse.success(response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<String>> me(
            @AuthenticationPrincipal CustomUserDetails userDetails ) {

        /* 직접 가져 오는 방식
            Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        */
        return ResponseEntity.ok(
                //ApiResponse.success(authentication.getName())
                ApiResponse.success(userDetails.getUsername()
                        + " / "
                        + userDetails.getUserId())
        );
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> adminOnly() {

        return ResponseEntity.ok(
                ApiResponse.success("ADMIN 권한이 있습니다.")
        );
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {

        List<UserResponse> users = userService.findAllUsers();

        return ResponseEntity.ok(
                ApiResponse.success(users)
        );
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody UserRoleUpdateRequest request ) {

        UserResponse response = userService.updateUserRole(id, request.getRole());

        return ResponseEntity.ok(
                ApiResponse.success(response)
        );
    }
}