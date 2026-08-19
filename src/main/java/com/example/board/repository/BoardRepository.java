package com.example.board.repository;

import com.example.board.entity.Board;
import com.example.board.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 제목 검색
    Page<Board> findByTitleContaining(String title, Pageable pageable);

    // 작성자 검색
    Page<Board> findByUser_UsernameContaining(String keyword, Pageable pageable);

    // 내용 검색
    Page<Board> findByContentContaining(String keyword, Pageable pageable);

    // 게시글 조회시 N+1 해결을 위한 fetch join 방식
    @Query(
            value = """
        SELECT b
        FROM Board b
        JOIN FETCH b.user
        """,
            countQuery = """
        SELECT COUNT(b)
        FROM Board b
        """
    )
    Page<Board> findAllWithUser(Pageable pageable);
}