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

        for (int i = 1; i <= 100; i++) {
            //Board board = new Board();
            //board.setTitle(i + "번째 게시글");
            //board.setWriter("관리자");
            //board.setContent("내용 " + i);

            Board board = Board.builder()
                    .title( i + "번째 게시글" )
                    .writer( "DataInitializer" )
                    .content( "내용 " + i )
                    .build();

            boardRepository.save(board);
        }

        boardRepository.findAll()
                .forEach(System.out::println);

        System.out.println("===== DataInitializer 종료 =====");
    }

}
