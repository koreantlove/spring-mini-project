package com.example.board.repository;

import com.example.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 제목 검색
    Page<Board> findByTitleContaining(String title, Pageable pageable);

    // 작성자 검색
    Page<Board> findByWriterContaining(String writer, Pageable pageable);


}