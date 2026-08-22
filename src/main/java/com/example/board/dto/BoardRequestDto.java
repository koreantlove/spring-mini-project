package com.example.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "게시글 작성 요청")
public class BoardRequestDto {

    @Schema(
            description = "게시글 제목",
            example = "Spring Boot JWT 인증 구현",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "제목은 필수입니다.")
    @Size(
            max = 100,
            message = "제목은 100자 이하로 입력해주세요."
    )
    private String title;

    @Schema(
            description = "게시글 내용",
            example = "JWT 기반 인증을 구현했습니다.",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "내용은 필수입니다.")
    @Size(
            max = 2000,
            message = "내용은 2000자 이하로 입력해주세요."
    )
    private String content;

}