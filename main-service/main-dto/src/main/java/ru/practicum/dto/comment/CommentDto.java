package ru.practicum.dto.comment;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDto {
    Long id;
    String content;
    Long user;
    Long event;
}
