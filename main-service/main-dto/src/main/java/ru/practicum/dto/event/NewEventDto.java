package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.event.annotation.ValidEventDate;

import java.time.LocalDateTime;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank @Size(max = 255, message = "Annotation must not be longer 255 letters")
    String annotation;
    @NotNull
    long categoryId;
    @NotBlank
    String description;
    @NotNull
    @ValidEventDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    @NotNull
    Location location;
    @JsonSetter(nulls = Nulls.SKIP) // если поле null, то
    Boolean paid = false; // по умолчанию false
    int participantLimit;
    Boolean requestModeration;
    @NotBlank
    String title;
}
