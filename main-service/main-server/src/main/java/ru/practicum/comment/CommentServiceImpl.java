package ru.practicum.comment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.DeleteCommentsDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.comment.UpdateCommentDto;
import ru.practicum.dto.event.enums.State;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.ConflictRelationsConstraintException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;


    @Override
    public CommentDto createComment(Long userId, Long eventId, NewCommentDto newCommentDto) {

        Event event = getEvent(eventId);

        if(!event.getState().equals(State.PUBLISHED))
            throw new ConflictRelationsConstraintException("Нельзя добавить комментарий если событие не опубликовано");

        User user = getUser(userId);

        Comment comment = Comment.builder()
                .created(LocalDateTime.now())
                .content(newCommentDto.getContent())
                .event(event)
                .user(user)
                .build();

        return  commentMapper.commentDtoToComment(commentRepository.save(comment));
    }


    @Override
    public CommentDto updateComment(Long userId, Long commentId, UpdateCommentDto updateCommentDto) {
        Comment comment = getComment(commentId);
        getCommentByUserId(userId, commentId);

        comment.setContent(updateCommentDto.getContent());

        return commentMapper.commentDtoToComment(commentRepository.save(comment));

    }

    @Override
    public void deleteCommentByUser(Long userId, Long commentId) {
        getUser(userId);
        getCommentByUserId(userId, commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        return commentMapper.commentDtoToComment(getComment(commentId));
    }

    @Override
    public List<CommentDto> getCommentsByUserId(Long userId) {
        getUser(userId);
        List<Comment> commentsList = commentRepository.findByUserId(userId);
        return commentsList.stream()
                .map(commentMapper::commentDtoToComment)
                .toList();
    }

    @Transactional
    @Override
    public void deleteCommentsByAdmin(DeleteCommentsDto deleteCommentsDto) {
        List<Long> commentListIds = commentRepository.findByIdIn(deleteCommentsDto.getCommentsIds()).stream()
                .map(Comment::getId)
                .toList();

        List<Long> commentIdsNotExist = deleteCommentsDto.getCommentsIds().stream()
                .filter(commentId -> !commentListIds.contains(commentId))
                .toList();

        if(!commentIdsNotExist.isEmpty())
            throw new NotFoundException("Комментарии с id: " + commentIdsNotExist + " не найдены");

        commentRepository.deleteByIdIn(deleteCommentsDto.getCommentsIds());

    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id: " + userId + " не существует")
        );
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("События с id: " + eventId + " не существует")
        );
    }

    private Comment getCommentByUserId(Long userId, Long commentId) {
        return commentRepository.findByUserIdAndId(userId, commentId).orElseThrow(
                () -> new ConflictRelationsConstraintException(
                        "Пользователю id: " + userId + " не принадлежит комментарий с id: " + commentId)
        );
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментария с id: " + commentId + " не существует")
        );
    }
}
