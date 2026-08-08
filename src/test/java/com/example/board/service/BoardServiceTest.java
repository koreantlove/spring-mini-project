package com.example.board.service;

import com.example.board.dto.BoardRequestDto;
import com.example.board.dto.BoardResponseDto;
import com.example.board.dto.BoardUpdateDto;
import com.example.board.entity.Board;
import com.example.board.exception.BoardNotFoundException;
import com.example.board.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.then;
import org.mockito.ArgumentCaptor;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock // 가짜 Repository를 만든다. // 실제 DB에 접근하지 않는다.
    private BoardRepository boardRepository;

    @InjectMocks // Mockito가 BoardService를 만들면서 Mock Repository를 주입
    private BoardService boardService;

    @Test
    void test() {
        assertEquals(1, 1);
    }

    @Test
    void 게시글_조회_성공() {

        // Given
        Long id = 1L;

        Board board = Board.builder()
                .title("1번째 게시글")
                .writer("DataInitializer")
                .content("내용 1")
                .build();

        given(boardRepository.findById(id))
                .willReturn(Optional.of(board));

        // When
        BoardResponseDto result = boardService.findById(id);

        // Then
        assertThat(result.getTitle())
                .isEqualTo("1번째 게시글");

        assertThat(result.getWriter())
                .isEqualTo("DataInitializer");

        assertThat(result.getContent())
                .isEqualTo("내용 1");
    }

    @Test
    void 존재하지_않는_게시글_조회() {

        // Given
        Long id = 999L;

        given(boardRepository.findById(id))
                .willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> boardService.findById(id) )
                .isInstanceOf(BoardNotFoundException.class)
                .hasMessage("게시글을 찾을 수 없습니다. id=999");
    }

    @Test
    void 게시글_등록() {

        // Given
        BoardRequestDto dto = new BoardRequestDto();

        dto.setTitle("테스트 제목");
        dto.setWriter("홍길동");
        dto.setContent("테스트 내용");

        Board savedBoard = Board.builder()
                .title("테스트 제목")
                .writer("홍길동")
                .content("테스트 내용")
                .build();

        given(boardRepository.save(any(Board.class)))
                .willReturn(savedBoard);

        // When
        boardService.save(dto);

        // Then
        then(boardRepository)
                .should()
                .save(any(Board.class));
    }

    @Test
    void 게시글_등록_데이터_검증() {

        // Given
        BoardRequestDto dto = new BoardRequestDto();

        dto.setTitle("테스트 제목");
        dto.setWriter("홍길동");
        dto.setContent("테스트 내용");

        // When
        boardService.save(dto);

        // Then
        ArgumentCaptor<Board> captor =
                ArgumentCaptor.forClass(Board.class);

        then(boardRepository)
                .should()
                .save(captor.capture());

        Board savedBoard = captor.getValue();

        assertThat(savedBoard.getTitle())
                .isEqualTo("테스트 제목");

        assertThat(savedBoard.getWriter())
                .isEqualTo("홍길동");

        assertThat(savedBoard.getContent())
                .isEqualTo("테스트 내용");
    }

    @Test
    void 게시글_수정() {

        // Given
        Long id = 1L;

        Board board = Board.builder()
                .title("기존 제목")
                .writer("홍길동")
                .content("기존 내용")
                .build();

        given(boardRepository.findById(id))
                .willReturn(Optional.of(board));

        BoardUpdateDto dto = new BoardUpdateDto();

        dto.setTitle("수정 제목");
        dto.setContent("수정 내용");

        // When
        boardService.update(id, dto);

        // Then
        assertThat(board.getTitle())
                .isEqualTo("수정 제목");

        assertThat(board.getContent())
                .isEqualTo("수정 내용");

        assertThat(board.getWriter())
                .isEqualTo("홍길동");
    }

    @Test
    void 게시글_삭제() {

        // Given
        Long id = 1L;

        Board board = Board.builder()
                .title("삭제할 글")
                .writer("홍길동")
                .content("내용")
                .build();

        given(boardRepository.findById(id))
                .willReturn(Optional.of(board));

        // When
        boardService.delete(id);

        // Then
        then(boardRepository)
                .should()
                .delete(board);
    }
}