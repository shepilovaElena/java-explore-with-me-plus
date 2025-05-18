package ru.practicum.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.event.enums.State;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor // чтобы Hibernate мог создать объект
@AllArgsConstructor // чтобы Builder работы при NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "Events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String annotation;
    long categoryId;
    long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;
    String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    long initiatorId;
    float location_lat;
    float location_lon;
    Boolean paid;
    long participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;
    Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    State state;
    String title;
    Long views;
}
