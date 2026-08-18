package com.example.board.controller;

import com.example.board.dto.BoardRequestDto;
import com.example.board.dto.BoardResponseDto;
import com.example.board.dto.BoardUpdateDto;
import com.example.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(required = false) String type,
                       @RequestParam(required = false) String keyword,
                       Model model){

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());

        Page<BoardResponseDto> boards;

        if (keyword == null || keyword.isBlank()) {

            boards = boardService.findAll(pageable);

        } else {

            boards = boardService.search(type, keyword, pageable);
        }

        model.addAttribute("boards", boards);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);

        return "board/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(required = false) String type,
                         @RequestParam(required = false) String keyword,
                         Model model){

        //model.addAttribute("board", boardService.findById(id));

        BoardResponseDto board = boardService.findById(id);

        model.addAttribute("board", board);

        model.addAttribute("page", page);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);

        return "board/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(required = false) String type,
                           @RequestParam(required = false) String keyword,
                           Model model) {

        model.addAttribute("board", boardService.findById(id));

        model.addAttribute("page", page);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);

        return "board/edit";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute BoardUpdateDto dto,
                         BindingResult bindingResult,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(required = false) String type,
                         @RequestParam(required = false) String keyword ) {

        if (bindingResult.hasErrors()) {
            return "board/edit";
        }
        boardService.update(id, dto,"");

        return "redirect:/boards/" + id
                + "?page=" + page
                + "&type=" + type
                + "&keyword=" + keyword;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(required = false) String type,
                        @RequestParam(required = false) String keyword) {

        boardService.delete(id,"");

        return "redirect:/boards"
                + "?page=" + page
                + "&type=" + type
                + "&keyword=" + keyword;
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
        boardService.save(dto,"");

        return "redirect:/boards";

        //그냥 index.html 로 돌아갈시엔, 새로 고침시 중복으로 데이터 입력 된다.
        //return "index";

    }
}