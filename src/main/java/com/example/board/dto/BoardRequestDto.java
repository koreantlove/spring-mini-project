package com.example.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequestDto {

    @NotBlank(message = "제목은 필수입니다.")
    @Size(
            max = 100,
            message = "제목은 100자 이하로 입력해주세요."
    )
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    @Size(
            max = 2000,
            message = "내용은 2000자 이하로 입력해주세요."
    )
    private String content;

}