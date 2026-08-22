package com.example.board.controller;

import com.example.board.dto.*;
import com.example.board.security.CustomUserDetails;
import com.example.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/boards")
public class BoardRestController {
    private final BoardService boardService;

    @Operation(
            summary = "게시글 목록 조회",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            },
            description = "게시글을 검색 조건과 페이징 조건에 따라 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "게시글 목록 조회 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자"
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BoardResponseDto>>> findAll(
            @Parameter(description = "검색 대상",example = "title",
                    schema = @Schema(
                        type = "string",
                        allowableValues = {"title", "content"} ))
            @RequestParam(required = false) String type,
            @Parameter(description = "검색어",example = "Spring")
            @RequestParam(required = false) String keyword,
            @ParameterObject
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
                ApiResponse.success(PageResponse.from(result))
        );
    }

    @Operation(
            summary = "게시글 상세 조회",
            description = "게시글 ID를 이용하여 게시글 상세 정보를 조회합니다."
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BoardResponseDto>> findById(
            @Parameter(
                    description = "조회할 게시글 ID",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {

        BoardResponseDto result =
                boardService.findById(id);

        return ResponseEntity.ok(
                ApiResponse.success(result)
        );
    }

    @Operation(
            summary = "게시글 작성",
            description = "로그인한 사용자가 게시글을 작성합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "게시글 작성 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 게시글 데이터"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자"
            )
    })
    @SecurityRequirement(name = "bearerAuth")
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

    @Operation(
            summary = "게시글 수정",
            description = "게시글 작성자 본인 또는 ADMIN 권한을 가진 사용자만 수정할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "게시글 수정 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "게시글 수정 권한 없음"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "게시글을 찾을 수 없음"
            )
    })
    @SecurityRequirement(name = "bearerAuth")
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

    @Operation(
            summary = "게시글 삭제",
            description = "게시글 작성자 본인 또는 ADMIN 권한을 가진 사용자만 삭제할 수 있습니다."
    )
    @SecurityRequirement(name = "bearerAuth")
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