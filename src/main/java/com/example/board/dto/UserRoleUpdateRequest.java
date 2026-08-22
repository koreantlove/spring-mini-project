package com.example.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRoleUpdateRequest {

    @Schema(
            description = "사용자 권한",
            allowableValues = {"ROLE_USER", "ROLE_ADMIN"},
            example = "ROLE_USER"
    )
    @NotBlank(message = "Role은 필수입니다.")
    private String role;
}