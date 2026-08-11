package com.example.board.repository;

import com.example.board.entity.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;


    @Test
    void 게시글_저장() {

        // Given
        Board board = Board.builder()
                .title("테스트 제목")
                .writer("홍길동")
                .content("테스트 내용")
                .build();

        // When
        Board savedBoard = boardRepository.save(board);

        // Then
        assertThat(savedBoard.getId())
                .isNotNull();

        assertThat(savedBoard.getTitle())
                .isEqualTo("테스트 제목");
    }

    @Test
    void 게시글_ID로_조회() {

        // Given
        Board board = Board.builder()
                .title("테스트 제목")
                .writer("홍길동")
                .content("테스트 내용")
                .build();

        Board savedBoard = boardRepository.save(board);

        // When
        Optional<Board> result =
                boardRepository.findById(savedBoard.getId());

        // Then
        assertThat(result)
                .isPresent();

        assertThat(result.get().getTitle())
                .isEqualTo("테스트 제목");
    }


    @Test
    void 게시글_전체_조회() {

        // Given
        boardRepository.save(
                Board.builder()
                        .title("첫 번째")
                        .writer("홍길동")
                        .content("내용1")
                        .build()
        );

        boardRepository.save(
                Board.builder()
                        .title("두 번째")
                        .writer("김철수")
                        .content("내용2")
                        .build()
        );

        // When
        List<Board> boards =
                boardRepository.findAll();

        // Then
        assertThat(boards)
                .hasSize(2);
    }


    @Test
    void 작성자로_게시글_조회() {

        // Given
        boardRepository.save(
                Board.builder()
                        .title("게시글1")
                        .writer("홍길동")
                        .content("내용1")
                        .build()
        );

        boardRepository.save(
                Board.builder()
                        .title("게시글2")
                        .writer("홍길동")
                        .content("내용2")
                        .build()
        );

        boardRepository.save(
                Board.builder()
                        .title("게시글3")
                        .writer("김철수")
                        .content("내용3")
                        .build()
        );

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Board> result =
                boardRepository.findByWriterContaining("홍길동", pageable);

        // Then
        assertThat(result)
                .hasSize(2);

        assertThat(result)
                .extracting(Board::getWriter)
                .containsOnly("홍길동");
    }


    @Test
    void 게시글_삭제() {

        // Given
        Board board = boardRepository.save(
                Board.builder()
                        .title("삭제할 게시글")
                        .writer("홍길동")
                        .content("내용")
                        .build()
        );

        Long id = board.getId();

        // When
        boardRepository.deleteById(id);

        // Then
        Optional<Board> result =
                boardRepository.findById(id);

        assertThat(result)
                .isEmpty();
    }


    @Test
    void 게시글_수정() {

        // Given
        Board board = boardRepository.save(
                Board.builder()
                        .title("기존 제목")
                        .writer("홍길동")
                        .content("기존 내용")
                        .build()
        );

        // When
        board.update(
                "수정된 제목",
                "수정된 내용"
        );

        boardRepository.flush();

        // Then
        Board result =
                boardRepository.findById(board.getId())
                        .orElseThrow();

        assertThat(result.getTitle())
                .isEqualTo("수정된 제목");

        assertThat(result.getContent())
                .isEqualTo("수정된 내용");
    }
}