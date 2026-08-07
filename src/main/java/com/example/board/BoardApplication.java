package com.example.board;

import com.example.board.entity.Board;
import com.example.board.repository.BoardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BoardApplication {
    // 2026-08-07 //
	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

	/*
	@Bean
	CommandLineRunner init(BoardRepository repository) {

		return args -> {

			Board board1 = new Board();
			board1.setTitle("첫 번째 글");
			board1.setWriter("관리자");
			board1.setContent("Spring Boot 시작!");
			repository.save(board1);

			Board board2 = new Board();
			board2.setTitle("두 번째 글");
			board2.setWriter("관리자");
			board2.setContent("2222222");
			repository.save(board2);

			Board board3 = new Board();
			board3.setTitle("세 번째 글");
			board3.setWriter("관리자");
			board3.setContent("333333333");
			repository.save(board3);

			repository.findAll()
					.forEach(System.out::println);


			repository.findById(2L);
			repository.deleteById(1L);
			repository.findAll().forEach(System.out::println);

		};

	}

	 */
}
