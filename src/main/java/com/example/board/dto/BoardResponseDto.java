package com.example.board.dto;

import com.example.board.entity.Board;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardResponseDto {

    private Long id;
    private String title;
    private String writer;
    private String content;
    private int viewCount;

    public static BoardResponseDto from(Board board) {
        return BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getUser().getUsername())
                .content(board.getContent())
                .viewCount(board.getViewCount())
                .build();
    }

}