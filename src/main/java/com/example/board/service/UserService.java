package com.example.board.service;

import com.example.board.dto.UserRequestDto;
import com.example.board.entity.User;
import com.example.board.exception.UserAlreadyExistsException;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void save(UserRequestDto requestDto) {

        if (userRepository.existsByUsername(
                requestDto.getUsername())) {

            throw new UserAlreadyExistsException(
                    "이미 존재하는 아이디입니다."
            );
        }

        User user = User.builder()
                .username(requestDto.getUsername())
                .password(requestDto.getPassword())
                .role("ROLE_USER")
                .build();

        userRepository.save(user);
    }
}