package ru.practicum.dto.event;

import category.CategoryDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    String annotation;
    CategoryDto category;
    String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    String eventDate;
    Location location;
    Boolean paid;
    int participantLimit;
    Boolean requestModeration;
    String title;
}
