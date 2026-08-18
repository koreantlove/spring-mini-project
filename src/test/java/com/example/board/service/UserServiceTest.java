package com.example.board.service;

import com.example.board.dto.UserRequestDto;
import com.example.board.entity.User;
import com.example.board.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void 회원가입() {

        // Given
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setUsername("hong");
        requestDto.setPassword("1234");

        given(userRepository.existsByUsername("hong"))
                .willReturn(false);

        // When
        userService.save(requestDto);

        // Then
        verify(userRepository)
                .save(any(User.class));
    }
}