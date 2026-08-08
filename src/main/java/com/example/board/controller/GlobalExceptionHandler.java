package com.example.board.controller;

import com.example.board.exception.BoardNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice // 여러 Controller에서 발생하는 예외를 한 곳에서 처리할 수 있게 해줌
@Order(2)
public class GlobalExceptionHandler {

    // BoardNotFoundException이 발생하면 이 메서드가 처리
    @ExceptionHandler(BoardNotFoundException.class)
    public String handleBoardNotFound(
            BoardNotFoundException e,
            Model model) {

        log.warn("게시글 조회 실패 - {}", e.getMessage());
        model.addAttribute("message", e.getMessage());

        return "error/404";
    }
}