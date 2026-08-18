package com.example.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "아이디는 필수입니다.")
    @Size(
            min = 4,
            max = 20,
            message = "아이디는 4자 이상 20자 이하로 입력해주세요."
    )
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(
            min = 8,
            max = 100,
            message = "비밀번호는 8자 이상 입력해주세요."
    )
    private String password;
}