package ru.practicum.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.request.enums.Status;

import java.util.List;

@Builder
@Data
public class EventRequestStatusUpdateRequest {
    @NotEmpty
    List<Long> requestIds;
    @NotEmpty
    Status status;
}
