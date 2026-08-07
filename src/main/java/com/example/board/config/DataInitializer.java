package com.example.board.config;

import com.example.board.entity.Board;
import com.example.board.repository.BoardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BoardRepository boardRepository;

    public DataInitializer(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public void run(String... args) {
        // 초기 데이터 생성

        System.out.println("===== DataInitializer 실행 =====");

        Board board1 = new Board();
        board1.setTitle("첫 번째 글");
        board1.setWriter("관리자");
        board1.setContent("Spring Boot 시작!");
        boardRepository.save(board1);

        Board board2 = new Board();
        board2.setTitle("두 번째 글");
        board2.setWriter("관리자");
        board2.setContent("2222222");
        boardRepository.save(board2);

        Board board3 = new Board();
        board3.setTitle("세 번째 글");
        board3.setWriter("관리자");
        board3.setContent("333333333");
        boardRepository.save(board3);

        boardRepository.findAll()
                .forEach(System.out::println);

        System.out.println("===== DataInitializer 종료 =====");
    }

}
