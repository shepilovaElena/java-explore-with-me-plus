package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByRequester_Id(Long requesterId);

    Optional<Request> findByRequester_IdAndEvent_Id(Long requesterId, Long eventId);

    List<Request> findByEvent_Id(Long eventId);

}
