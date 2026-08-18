package com.example.board.dto;

import com.example.board.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private final Long id;
    private final String content;
    private final String writer;
    private final LocalDateTime createdDate;

    public CommentResponseDto(
            Long id,
            String content,
            String writer,
            LocalDateTime createdDate
    ) {
        this.id = id;
        this.content = content;
        this.writer = writer;
        this.createdDate = createdDate;
    }

    public static CommentResponseDto from(Comment comment) {

        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getUsername(),
                comment.getCreatedDate()
        );
    }
}