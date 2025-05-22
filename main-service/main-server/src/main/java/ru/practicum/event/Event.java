package ru.practicum.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.event.enums.State;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor // чтобы Hibernate мог создать объект
@AllArgsConstructor // чтобы Builder работы при NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(columnDefinition = "TEXT")
    String annotation;
    @Column(name = "category_id")
    @Transient
    long categoryId;
    @Column(name = "confirmed_requests")
    @Transient
    long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_on")
    LocalDateTime createdOn;
    @Column(columnDefinition = "TEXT")
    String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    @Column(name = "initiator_id")
    long initiatorId;
    float location_lat;
    float location_lon;
    Boolean paid;
    long participantLimit;
    @Column(name = "published_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    State state;
    String title;
    @Transient
    Long views;
}
