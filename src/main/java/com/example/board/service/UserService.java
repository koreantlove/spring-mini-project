package com.example.board.service;

import com.example.board.dto.UserLoginRequestDto;
import com.example.board.dto.UserRequestDto;
import com.example.board.dto.UserResponse;
import com.example.board.entity.User;
import com.example.board.exception.InvalidLoginException;
import com.example.board.exception.ResourceNotFoundException;
import com.example.board.exception.UserAlreadyExistsException;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void save(UserRequestDto requestDto) {

        if (userRepository.existsByUsername(
                requestDto.getUsername())) {

            throw new UserAlreadyExistsException(
                    "이미 존재하는 아이디입니다."
            );
        }

        String encodedPassword =
                passwordEncoder.encode(requestDto.getPassword());

        User user = User.builder()
                .username(requestDto.getUsername())
                .password(encodedPassword)
                .role("ROLE_USER")
                .build();

        userRepository.save(user);
    }

    /* 로그인 기능 변경 ( 직접 -> authenticationManager 사용 )
    @Transactional
    public void login(UserLoginRequestDto requestDto) {


        User user = userRepository.findByUsername(
                requestDto.getUsername()
        ).orElseThrow(() ->
                new InvalidLoginException("아이디 또는 비밀번호가 올바르지 않습니다.")
        );

        if (!passwordEncoder.matches(
                requestDto.getPassword(),
                user.getPassword())) {

            throw new InvalidLoginException(
                    "아이디 또는 비밀번호가 올바르지 않습니다."
            );
        }
    }*/
    public Authentication login(UserLoginRequestDto requestDto) {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        requestDto.getUsername(),
                        requestDto.getPassword()
                );

        return authenticationManager.authenticate(authentication);
    }

    public List<UserResponse> findAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    @Transactional
    public UserResponse updateUserRole( Long userId, String role) {
        if (!role.equals("ROLE_USER") && !role.equals("ROLE_ADMIN")) {
            throw new IllegalArgumentException(
                    "올바르지 않은 Role입니다."
            );
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "사용자를 찾을 수 없습니다."
                        )
                );

        user.setRole(role);
        User savedUser = userRepository.save(user);

        return UserResponse.from(savedUser);
    }
}