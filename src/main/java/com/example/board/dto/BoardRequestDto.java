package com.example.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequestDto {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "작성자는 필수입니다.")
    private String writer;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

}