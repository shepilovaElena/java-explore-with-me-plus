package ru.practicum.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("SELECT e FROM Event e " +
            "WHERE (:text IS NULL OR LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "AND (:categories IS NULL OR e.categoryId IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (:rangeStart IS NULL OR e.eventDate > :rangeStart) " +
            "AND (:rangeEnd IS NULL OR e.eventDate < :rangeEnd) " +
            "AND (:onlyAvailable IS NULL OR e.participantLimit > 0)" +
            "AND (e.requestModeration = TRUE)")
    Page<Event> getEvents(@Param("text") String text,
                          @Param("categories") List<Integer> categories,
                          @Param("paid") Boolean paid,
                          @Param("rangeStart") String rangeStart,
                          @Param("rangeEnd") String rangeEnd,
                          @Param("onlyAvailable") Boolean onlyAvailable,
                          Pageable pageable);
}
