package com.example.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardUpdateDto {

    private String title;
    private String writer;
    private String content;

}