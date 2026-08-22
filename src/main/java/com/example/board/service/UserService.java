package com.example.board.service;

import com.example.board.dto.*;
import com.example.board.entity.User;
import com.example.board.exception.BusinessException;
import com.example.board.exception.InvalidLoginException;
import com.example.board.exception.ResourceNotFoundException;
import com.example.board.exception.UserAlreadyExistsException;
import com.example.board.repository.UserRepository;
import com.example.board.security.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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
                .role( UserRole.USER.getAuthority() )
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
    public JwtResponse login(UserLoginRequestDto requestDto) {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        requestDto.getUsername(),
                        requestDto.getPassword()
                );

        Authentication authenticated =
                authenticationManager.authenticate(authentication);

        String role = authenticated.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("");


        //return jwtService.createToken(authenticated.getName(), role);

        String accessToken = jwtService.createToken( authenticated.getName(), role );
        String refreshToken =jwtService.generateRefreshToken( authenticated.getName() );

        return new JwtResponse(accessToken, refreshToken);
    }

    public String refreshAccessToken(String refreshToken) {

        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw new InvalidLoginException("유효하지 않은 Refresh Token입니다.");
        }

        String username = jwtService.getUsername(refreshToken);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new InvalidLoginException("사용자를 찾을 수 없습니다.")
                );

        return jwtService.createToken(user.getUsername(), user.getRole());
    }

    public List<UserResponse> findAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    @Transactional
    public UserResponse updateUserRole( Long userId, String role) {

        UserRole newRole = parseRole(role);

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "사용자를 찾을 수 없습니다."
                        )
                );

        String currentRole = user.getRole();

        // 같은 Role이면 변경할 필요 없음
        if (currentRole.equals(newRole.getAuthority())) {
            return UserResponse.from(user);
        }

        // ADMIN → USER로 변경하는 경우
        if (UserRole.ADMIN.getAuthority().equals(currentRole)
                && UserRole.USER.equals(newRole)) {

            long adminCount = userRepository.countByRole( UserRole.ADMIN.getAuthority() );

            if (adminCount <= 1) {
                throw new BusinessException(
                        "마지막 ADMIN의 권한은 변경할 수 없습니다."
                );
            }
        }

        user.setRole(newRole.getAuthority());
        User savedUser = userRepository.save(user);

        return UserResponse.from(savedUser);
    }

    private UserRole parseRole(String role) {

        for (UserRole userRole : UserRole.values()) {

            if (userRole.getAuthority().equals(role)) {
                return userRole;
            }
        }

        throw new IllegalArgumentException(
                "올바르지 않은 Role입니다."
        );
    }
}