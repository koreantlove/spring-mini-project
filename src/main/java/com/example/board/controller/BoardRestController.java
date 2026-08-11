package com.example.board.controller;

import com.example.board.dto.BoardRequestDto;
import com.example.board.dto.BoardResponseDto;
import com.example.board.dto.BoardUpdateDto;
import com.example.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardRestController {
    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<Page<BoardResponseDto>> findAll(
            Pageable pageable) {

        Page<BoardResponseDto> result =
                boardService.findAll(pageable);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> findById(
            @PathVariable Long id) {

        BoardResponseDto result =
                boardService.findById(id);

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Void> save(
            @Valid @RequestBody BoardRequestDto dto) {

        boardService.save(dto);

        //return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.CREATED).build();

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
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @Valid @RequestBody BoardUpdateDto dto) {

        boardService.update(id, dto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        boardService.delete(id);

        return ResponseEntity.noContent().build();
    }


}