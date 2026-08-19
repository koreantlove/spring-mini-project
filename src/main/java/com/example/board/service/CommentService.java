package com.example.board.service;

import com.example.board.dto.CommentRequestDto;
import com.example.board.dto.CommentResponseDto;
import com.example.board.entity.Board;
import com.example.board.entity.Comment;
import com.example.board.entity.User;
import com.example.board.exception.BoardNotFoundException;
import com.example.board.exception.CommentNotFoundException;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.CommentRepository;
import com.example.board.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void save(
            Long boardId,
            CommentRequestDto dto,
            Authentication authentication
    ) {

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() ->
                        new BoardNotFoundException(boardId)
                );

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .createdDate(LocalDateTime.now())
                .board(board)
                .user(user)
                .build();

        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> findByBoardId(
            Long boardId
    ) {

        /*return commentRepository
                .findByBoardIdOrderByIdAsc(boardId)
                .stream()
                .map(CommentResponseDto::from)
                .toList();*/
        return commentRepository
                .findByBoardIdWithUser(boardId)
                .stream()
                .map(CommentResponseDto::from)
                .toList();
    }

    @Transactional
    public void update( Long boardId, Long commentId, CommentRequestDto dto, Authentication authentication) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new CommentNotFoundException(commentId )
                );
        validateCommentBoard(comment, boardId );
        checkCommentPermission(comment, authentication);

        comment.update(dto.getContent());
    }

    @Transactional
    public void delete( Long boardId, Long commentId,Authentication authentication ) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new CommentNotFoundException(commentId )
                );
        validateCommentBoard(comment, boardId );
        checkCommentPermission(comment, authentication);
        commentRepository.delete(comment);
    }

    private void checkCommentPermission( Comment comment, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN")
                );

        boolean isWriter = comment.getUser().getUsername().equals(authentication.getName());

        if (!isAdmin && !isWriter) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    private void validateCommentBoard( Comment comment,Long boardId) {

        if (!comment.getBoard().getId().equals(boardId)) {
            throw new IllegalArgumentException(
                    "해당 게시글의 댓글이 아닙니다."
            );
        }
    }

}