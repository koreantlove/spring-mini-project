package com.example.board.controller;

import com.example.board.dto.*;
import com.example.board.security.CustomUserDetails;
import com.example.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardRestController {
    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BoardResponseDto>>> findAll(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            @PageableDefault(
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {

        Page<BoardResponseDto> result;

        if (keyword == null || keyword.isBlank()) {
            result = boardService.findAll(pageable);

        } else {
            result = boardService.search(
                    type,
                    keyword.trim(),
                    pageable
            );
        }

        return ResponseEntity.ok(
                ApiResponse.success(result)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BoardResponseDto>> findById(
            @PathVariable Long id) {

        BoardResponseDto result =
                boardService.findById(id);

        return ResponseEntity.ok(
                ApiResponse.success(result)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> save(
            @Valid @RequestBody BoardRequestDto dto,
            Authentication authentication) {

        boardService.save(dto, authentication);

        //return ResponseEntity.ok().build();
        //return ResponseEntity.status(HttpStatus.CREATED).build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success());

        /*
        200 OK → 정상적인 요청 처리
        201 Created→ 새로운 리소스 생성
        400 Bad Request→ 잘못된 요청
        401 Unauthorized→ 인증 필요
        403 Forbidden→ 권한 없음
        404 Not Found→ 리소스 없음
        500 Internal Server Error→ 서버 내부 오류
         */
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable Long id,
            @Valid @RequestBody BoardUpdateDto dto,
            Authentication authentication) {

        boardService.update(id, dto, authentication);

        //return ResponseEntity.ok().build();
        return ResponseEntity.ok(
                ApiResponse.success()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            Authentication authentication ) {

        boardService.delete(id, authentication );

        //return ResponseEntity.noContent().build();
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}