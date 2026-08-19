package com.example.board.repository;

import com.example.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository
        extends JpaRepository<Comment, Long> {

    List<Comment> findByBoardIdOrderByIdAsc(Long boardId);

    @Query("""
    SELECT c
    FROM Comment c
    JOIN FETCH c.user
    WHERE c.board.id = :boardId
    ORDER BY c.id ASC
    """)
    List<Comment> findByBoardIdWithUser(@Param("boardId") Long boardId);
}