package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.comment.CommentDto;

@RestController
@RequiredArgsConstructor
public class CommentControllerPublic {

    private final CommentService commentService;

    //findComments

    @GetMapping("/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getCommentByID(@PathVariable Long commentId) {

        return commentService.getCommentById(commentId);
    }
}
