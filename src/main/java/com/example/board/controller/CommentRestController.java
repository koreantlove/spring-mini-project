package com.example.board.controller;

import com.example.board.dto.ApiResponse;
import com.example.board.dto.CommentRequestDto;
import com.example.board.dto.CommentResponseDto;
import com.example.board.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/boards/{boardId}/comments")
public class CommentRestController {

    private final CommentService commentService;

    @Operation(
            summary = "댓글 목록 조회",
            description = "특정 게시글에 작성된 댓글을 조회합니다."
    )
    @SecurityRequirement(name = "bearerAuth")
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

    @Operation(
            summary = "댓글 작성",
            description = "특정 게시글에 댓글을 작성합니다."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> save(
            @Parameter(
                    description = "댓글을 작성할 게시글 ID",
                    example = "1",
                    required = true
            )
            @PathVariable Long boardId,
            @Valid @RequestBody CommentRequestDto dto,
            Authentication authentication ) {

        commentService.save( boardId, dto, authentication );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success());
    }

    @Operation(
            summary = "댓글 수정",
            description = "댓글 작성자 본인 또는 ADMIN 권한을 가진 사용자만 수정할 수 있습니다."
    )
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDto dto,
            Authentication authentication ) {

        commentService.update( boardId, commentId, dto, authentication );

        return ResponseEntity.ok( ApiResponse.success() );
    }

    @Operation(
            summary = "댓글 삭제",
            description = "댓글 작성자 본인 또는 ADMIN 권한을 가진 사용자만 삭제할 수 있습니다."
    )
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            Authentication authentication ) {

        commentService.delete( boardId, commentId, authentication );
        return ResponseEntity.noContent().build();
    }

}