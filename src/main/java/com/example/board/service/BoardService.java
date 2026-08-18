package com.example.board.service;

import com.example.board.dto.BoardRequestDto;
import com.example.board.dto.BoardResponseDto;
import com.example.board.dto.BoardUpdateDto;
import com.example.board.entity.Board;
import com.example.board.entity.User;
import com.example.board.exception.BoardNotFoundException;
import com.example.board.repository.BoardRepository;
import com.example.board.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    /*public List<Board> findAll() {
        return boardRepository.findAll();
    }*/

    // 조회 전용
    @Transactional(readOnly = true)
    public Page<BoardResponseDto> findAll(Pageable pageable) {

        return boardRepository.findAll(pageable)
                .map(BoardResponseDto::from);
    }

    // 게시글 저장
    @Transactional
    public void save(BoardRequestDto dto, Authentication authentication) {

        /*if (dto.getTitle().isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }*/
        log.info("게시글 등록 시작 - title={}", dto.getTitle());

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        Board board = Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .createdDate(LocalDateTime.now())
                .build();

        boardRepository.save(board);
        log.info("게시글 등록 완료 - id={}", board.getId());
    }

    // 상세 조회
    // 조회 전용
    @Transactional(readOnly = true)
    public BoardResponseDto findById(Long id){

        //Board board = boardRepository.findById(id)
        //        .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));
        log.info("게시글 조회 시작 - id={}", id);
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> {
                            log.warn("게시글을 찾을 수 없음 - id={}", id);
                            return new BoardNotFoundException(id);
                        }
                );
        log.info("게시글 조회 완료 - id={}", id);
        return BoardResponseDto.from(board);
    }

    @Transactional
    public void update(Long id, BoardUpdateDto dto, Authentication authentication) {

        //Board board = boardRepository.findById(id)
        //        .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));

        log.info("게시글 수정 시작 - id={}", id);

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> {
                            log.warn("수정 대상 게시글 없음 - id={}", id);
                            return new BoardNotFoundException(id);
                        }
                );

        checkBoardPermission(board, authentication);

        //board.setTitle(dto.getTitle());
        //board.setContent(dto.getContent());

        board.update(
                dto.getTitle(),
                dto.getContent()
        );

        log.info("게시글 수정 완료 - id={}", id);
        // 별도의 업데이트 문이 없다.
        // findById()로 조회한 Board는 관리(Managed) 되는 객체이다.
        // 이 객체의 값을 변경하면 JPA가 변경을 감지한다.
        // 트랜잭션이 끝날 때 를 자동으로 실행한다.
        // 이것을 Dirty Checking(변경 감지) 라고 한다.

    }

    @Transactional
    public void delete(Long id, Authentication authentication) {

        //Board board = boardRepository.findById(id)
        //        .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));
        log.info("게시글 삭제 시작 - id={}", id);

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("삭제 대상 게시글 없음 - id={}", id);
                    return new BoardNotFoundException(id);
                } );

        checkBoardPermission(board, authentication);

        boardRepository.delete(board);

        log.info("게시글 삭제 완료 - id={}", id);
    }

    @Transactional(readOnly = true)
    public Page<BoardResponseDto> search(
            String type,
            String keyword,
            Pageable pageable) {

        log.info(
                "게시글 검색 - type={}, keyword={}, page={}",
                type,  keyword, pageable.getPageNumber()
        );

        Page<Board> result;

        switch (type) {
            case "writer":
                result = boardRepository.findByUser_UsernameContaining(keyword, pageable);
                break;
            case "content":
                result = boardRepository.findByContentContaining(keyword, pageable);
                break;
            case "title":
                result = boardRepository.findByTitleContaining(keyword, pageable);
                break;
            default:
                throw new IllegalArgumentException(
                        "검색 타입이 올바르지 않습니다."
                );
        }

        log.info( "게시글 검색 완료 - count={}", result.getTotalElements() );

        return result.map(BoardResponseDto::from);
    }

    private void checkBoardPermission(
            Board board,
            Authentication authentication
    ) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN")
                );

        boolean isWriter =
                board.getUser().getUsername().equals(authentication.getName());

        if (!isAdmin && !isWriter) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }
}

