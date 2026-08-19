package com.example.board.config;

import com.example.board.entity.Board;
import com.example.board.entity.Comment;
import com.example.board.entity.User;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.CommentRepository;
import com.example.board.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommentRepository commentRepository;

    public DataInitializer(BoardRepository boardRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           CommentRepository commentRepository ) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.commentRepository = commentRepository;
    }

    @Override
    public void run(String... args) {
        // 초기 데이터 생성

        System.out.println("===== DataInitializer 실행 =====");

        String encodedPassword = passwordEncoder.encode("1234");

        /*User user = new User("test01",encodedPassword,"ROLE_USER");
        userRepository.save(user);

        User user2 = new User("test02",encodedPassword,"ROLE_USER");
        userRepository.save(user2);
        */

        User admin = new User("admin", encodedPassword,"ROLE_ADMIN");
        userRepository.save(admin);

        for (int i = 1; i <= 10; i++) {
            //Board board = new Board();
            //board.setTitle(i + "번째 게시글");
            //board.setWriter("관리자");
            //board.setContent("내용 " + i);
            User user = new User("test"+i,encodedPassword,"ROLE_USER");
            userRepository.save(user);

            Board board = Board.builder()
                    .title( i + "번째 게시글" )
                    .user( user )
                    .content( "내용 " + i )
                    .build();

            boardRepository.save(board);

            Comment comment = Comment.builder()
                    .content("댓글 이미 생성")
                    .user(user)
                    .board(board).build();
            commentRepository.save(comment);

        }
        boardRepository.findAll()
                .forEach(System.out::println);


        System.out.println("===== DataInitializer 종료 =====");
    }

}
