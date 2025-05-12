package ru.practicum.dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.practicum.dto.category.CategoryDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    @NotBlank
    String annotation;
    @NotNull
    CategoryDto category;
    int confirmedRequests;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    LocalDateTime eventDate;
    int id;
    @NotNull
    UserShortDto initiator;
    @NotNull
    Boolean paid;
    @NotBlank
    String title;
    int views;
}
