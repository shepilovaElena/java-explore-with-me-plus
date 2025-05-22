package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.CategoryService;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.UserService;

import java.util.List;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationMapperCustom mapper;
    private final EventRepository eventRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    @Override
    public CompilationDto create(NewCompilationDto dto) {
        Set<Event> events = new HashSet<>();
        if (dto.getEvents() != null && !dto.getEvents().isEmpty()) {
            events.addAll(eventRepository.findAllById(dto.getEvents()));
        }

        Compilation compilation = Compilation.builder()
                .title(dto.getTitle())
                .pinned(Boolean.TRUE.equals(dto.getPinned()))
                .events(events)
                .build();

        if (dto.getPinned() == null)
            compilation.setPinned(false);

        Compilation saved = compilationRepository.save(compilation);
        List<EventShortDto> eventDtos = mapToShortDtos(saved.getEvents());

        return mapper.toDto(saved, eventDtos);
    }

    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequest dto) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found"));

        if (dto.getTitle() != null) {
            compilation.setTitle(dto.getTitle());
        }
        if (dto.getPinned() != null) {
            compilation.setPinned(dto.getPinned());
        }
        if (dto.getEvents() != null) {
            Set<Event> events = new HashSet<>(eventRepository.findAllById(dto.getEvents()));
            compilation.setEvents(events);
        }

        Compilation updated = compilationRepository.save(compilation);
        return mapper.toDto(updated, mapToShortDtos(updated.getEvents()));
    }

    @Override
    public void delete(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Compilation not found");
        }
        compilationRepository.deleteById(compId);
    }

    @Override
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        PageRequest page = PageRequest.of(from / size, size);
        List<Compilation> comps = (pinned != null)
                ? compilationRepository.findAllByPinned(pinned, page)
                : compilationRepository.findAll(page).getContent();

        return comps.stream()
                .map(comp -> mapper.toDto(comp, mapToShortDtos(comp.getEvents())))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getById(Long compId) {
        Compilation comp = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation not found"));
        return mapper.toDto(comp, mapToShortDtos(comp.getEvents()));
    }

    private List<EventShortDto> mapToShortDtos(Collection<Event> events) {
        return events.stream()
                .map(event -> EventShortDto.builder()
                        .id((int) event.getId()) // уже Long
                        .annotation(event.getAnnotation())
                        .title(event.getTitle())
                        .eventDate(event.getEventDate())
                        .paid(event.getPaid())
                        .confirmedRequests((int) event.getConfirmedRequests())
                        .views(event.getViews())
                        .initiator(userService.getUserShortById(event.getInitiatorId()))
                        .category(categoryService.getCategoryDtoById(event.getCategoryId()))
                        .build())
                .toList();
    }

}
