package com.example.board.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional  // 테스트 후에 rollback
class BoardRestControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void 게시글_목록_조회() throws Exception {

        // When
        ResultActions result = mockMvc.perform(
                get("/api/boards")
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray());;
    }

    @Test
    void 게시글_상세_조회() throws Exception {

        // Given
        Long id = 1L;

        // When
        ResultActions result = mockMvc.perform(
                get("/api/boards/{id}", id)
        );

        // Then
        result.andExpect(status().isOk());
    }

    @Test
    void 게시글_등록() throws Exception {

        // Given
        String requestBody = """
            {
                "title": "테스트 게시글",
                "writer": "홍길동",
                "content": "테스트 내용"
            }
            """;

        // When
        ResultActions result = mockMvc.perform(
                post("/api/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then
        result.andExpect(status().isCreated());
    }


    @Test
    void 제목이_없으면_게시글_등록_실패() throws Exception {

        // Given
        String requestBody = """
            {
                "title": "",
                "writer": "홍길동",
                "content": "내용"
            }
            """;

        // When
        ResultActions result = mockMvc.perform(
                post("/api/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(
                        jsonPath("$.message")
                                .value("입력값이 올바르지 않습니다.")
                )
                .andExpect(
                        jsonPath("$.errors.title")
                                .exists()
                );
    }

    @Test
    void 존재하지_않는_게시글_조회() throws Exception {

        // Given
        Long id = 99999L;

        // When
        ResultActions result = mockMvc.perform(
                get("/api/boards/{id}", id)
        );

        // Then
        result.andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.message")
                                .exists()
                );
    }

    @Test
    void 게시글_수정() throws Exception {

        // Given
        Long id = 1L;

        String requestBody = """
            {
                "title": "수정된 게시글",
                "writer": "홍길동",
                "content": "수정된 내용"
            }
            """;

        // When
        ResultActions result = mockMvc.perform(
                put("/api/boards/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then
        result.andExpect(status().isOk());
    }

    @Test
    void 게시글_삭제() throws Exception {

        // Given
        Long id = 1L;

        // When
        ResultActions result = mockMvc.perform(
                delete("/api/boards/{id}", id)
        );

        // Then
        result.andExpect(status().isNoContent());
    }
}