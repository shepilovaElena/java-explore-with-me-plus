package ru.practicum.comment;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.comment.UpdateCommentDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentControllerPrivate {



    @PostMapping("/users/{userId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable Long userId,
                                    @RequestParam Long eventId,
                                    @Valid @RequestBody NewCommentDto newCommentDto) {

    }

    @PatchMapping("/users/{userId}/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long commentId,
                                    @Valid @RequestBody UpdateCommentDto updateCommentDto) {

    }

    @DeleteMapping("/users/{userId}/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {

    }


    @GetMapping("/users/{userId}/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getCommentByID(@PathVariable Long userId,
                              @PathVariable Long commentId) {

    }

    @GetMapping("/users/{userId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getCommentByUserId(@PathVariable Long userId) {

    }




}
