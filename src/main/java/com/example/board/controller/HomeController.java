package com.example.board.controller;

import com.example.board.dto.BoardRequestDto;
import com.example.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/boards";
    }

    /*

    private final BoardService boardService;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("boards", boardService.findAll());
        return "index";
    }

    @GetMapping("/boards/new")
    public String writeForm() {
        return "write";
    }

    @PostMapping("/boards")
    public String save(@ModelAttribute BoardRequestDto dto) {
        boardService.save(dto);

        return "redirect:/";

        //그냥 index.html 로 돌아갈시엔, 새로 고침시 중복으로 데이터 입력 된다.
        //return "index";

    }
    */

}
