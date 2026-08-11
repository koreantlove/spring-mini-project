package com.example.board.controller;

import com.example.board.dto.BoardRequestDto;
import com.example.board.dto.BoardResponseDto;
import com.example.board.exception.BoardNotFoundException;
import com.example.board.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.BDDMockito.given;

@WebMvcTest(BoardRestController.class)
public class BoardRestControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BoardService boardService;

    @Test
    void 게시글_상세_조회() throws Exception {

        // Given
        Long id = 1L;

        BoardResponseDto response = BoardResponseDto.builder()
                .id(1L)
                .title("1번째 게시글")
                .writer("홍길동")
                .content("내용").build();

        given(boardService.findById(id))
                .willReturn(response);

        // When
        ResultActions result = mockMvc.perform(
                get("/api/boards/{id}", id)
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("1번째 게시글"))
                .andExpect(jsonPath("$.data.writer").value("홍길동"))
                .andExpect(jsonPath("$.data.content").value("내용"));

        verify(boardService)
                .findById(id);
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

        verify(boardService)
                .save(any(BoardRequestDto.class));
    }

    @Test
    void 제목이_없으면_Service를_호출하지_않는다() throws Exception {

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
        result.andExpect(status().isBadRequest());

        verify(boardService, never())
                .save(any(BoardRequestDto.class));
    }

    @Test
    void 존재하지_않는_게시글_조회() throws Exception {

        // Given
        Long id = 99999L;

        given(boardService.findById(id))
                .willThrow(new BoardNotFoundException(id));

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
}
