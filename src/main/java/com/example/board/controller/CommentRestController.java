package com.example.board.controller;

import com.example.board.dto.ApiResponse;
import com.example.board.dto.CommentRequestDto;
import com.example.board.dto.CommentResponseDto;
import com.example.board.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards/{boardId}/comments")
public class CommentRestController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponseDto>>> findAll(
            @PathVariable Long boardId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        commentService.findByBoardId(boardId)
                )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> save(
            @PathVariable Long boardId,
            @Valid @RequestBody CommentRequestDto dto,
            Authentication authentication ) {

        commentService.save( boardId, dto, authentication );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success());
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDto dto,
            Authentication authentication ) {

        commentService.update( boardId, commentId, dto, authentication );

        return ResponseEntity.ok( ApiResponse.success() );
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            Authentication authentication ) {

        commentService.delete( boardId, commentId, authentication );
        return ResponseEntity.noContent().build();
    }

}