package com.example.board.controller;

import com.example.board.dto.BoardRequestDto;
import com.example.board.dto.BoardUpdateDto;
import com.example.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public String list(Model model){

        model.addAttribute("boards", boardService.findAll());

        return "board/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         Model model){

        model.addAttribute("board",
                boardService.findById(id));

        return "board/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {

        model.addAttribute("board", boardService.findById(id));

        return "board/edit";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute BoardUpdateDto dto) {

        boardService.update(id, dto);

        return "redirect:/boards/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {

        boardService.delete(id);

        return "redirect:/boards";
    }

    @GetMapping("/new")
    public String writeForm(Model model) {

        model.addAttribute("boardRequestDto",
                new BoardRequestDto());

        return "board/write";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute BoardRequestDto dto,
                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "board/write";
        }
        boardService.save(dto);

        return "redirect:/boards";

        //그냥 index.html 로 돌아갈시엔, 새로 고침시 중복으로 데이터 입력 된다.
        //return "index";

    }
}