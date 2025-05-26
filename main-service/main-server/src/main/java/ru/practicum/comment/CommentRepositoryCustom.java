package ru.practicum.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {

    Page<Comment> getComments(String content, Long userId, Long eventId,
                              String rangeStart, String rangeEnd, Integer from, Integer size,
                              Pageable pageable);
}
