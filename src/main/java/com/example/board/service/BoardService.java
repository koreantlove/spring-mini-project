package com.example.board.service;

import com.example.board.dto.BoardRequestDto;
import com.example.board.dto.BoardResponseDto;
import com.example.board.dto.BoardUpdateDto;
import com.example.board.entity.Board;
import com.example.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    /*public List<Board> findAll() {
        return boardRepository.findAll();
    }*/

    public Page<BoardResponseDto> findAll(Pageable pageable) {

        return boardRepository.findAll(pageable)
                .map(BoardResponseDto::new);
    }

    public void save(BoardRequestDto dto) {

        /*if (dto.getTitle().isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }*/

        Board board = new Board();

        board.setTitle(dto.getTitle());
        board.setWriter(dto.getWriter());
        board.setContent(dto.getContent());

        boardRepository.save(board);
    }

    public Board findById(Long id){

        return boardRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("게시글이 존재하지 않습니다."));
    }

    @Transactional
    public void update(Long id, BoardUpdateDto dto) {

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));

        board.setTitle(dto.getTitle());
        board.setWriter(dto.getWriter());
        board.setContent(dto.getContent());

        // 별도의 업데이트 문이 없다.
        // findById()로 조회한 Board는 관리(Managed) 되는 객체이다.
        // 이 객체의 값을 변경하면 JPA가 변경을 감지한다.
        // 트랜잭션이 끝날 때 를 자동으로 실행한다.
        // 이것을 Dirty Checking(변경 감지) 라고 한다.

    }

    @Transactional
    public void delete(Long id) {

        Board board = boardRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("게시글이 없습니다."));

        boardRepository.delete(board);
    }
}

