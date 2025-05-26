package ru.practicum.comment;


import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.DeleteCommentsDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.comment.UpdateCommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentDto updateComment(Long userId, Long commentId, UpdateCommentDto updateCommentDto);

    void deleteCommentByUser(Long userId, Long commentId);

    CommentDto getCommentById(Long commentId);

    List<CommentDto> getCommentsByUserId(Long userId);

    void deleteCommentsByAdmin(DeleteCommentsDto deleteCommentsDto);

}
