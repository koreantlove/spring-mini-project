package com.example.board.repository;

import com.example.board.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void 사용자_저장() {

        // Given
        User user = User.builder()
                .username("hong")
                .password("1234")
                .role("ROLE_USER")
                .build();

        // When
        User result =
                userRepository.save(user);

        // Then
        assertThat(result.getId())
                .isNotNull();

        assertThat(result.getUsername())
                .isEqualTo("hong");
    }

    @Test
    void username으로_사용자_조회() {

        // Given
        User user = User.builder()
                .username("hong")
                .password("1234")
                .role("ROLE_USER")
                .build();

        userRepository.save(user);

        // When
        Optional<User> result =
                userRepository.findByUsername("hong");

        // Then
        assertThat(result)
                .isPresent();

        assertThat(result.get().getUsername())
                .isEqualTo("hong");
    }

    @Test
    void username_중복_확인() {

        // Given
        userRepository.save(
                User.builder()
                        .username("hong")
                        .password("1234")
                        .role("ROLE_USER")
                        .build()
        );

        // When
        boolean result =
                userRepository.existsByUsername("hong");

        // Then
        assertThat(result)
                .isTrue();
    }
}