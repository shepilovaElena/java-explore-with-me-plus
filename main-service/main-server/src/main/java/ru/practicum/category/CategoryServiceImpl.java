package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ConflictPropertyConstraintException;
import ru.practicum.exception.ConflictRelationsConstraintException;
import ru.practicum.exception.NotFoundException;

import java.util.List;

import static ru.practicum.category.CategoryMapperCustom.toDto;
import static ru.practicum.category.CategoryMapperCustom.toEntity;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto create(NewCategoryDto dto) {
        if (repository.existsByName(dto.getName())) {
            throw new ConflictPropertyConstraintException("Category with name already exists");
        }
        Category category = toEntity(dto);
        return CategoryMapperCustom.toDto(repository.save(category));
    }

    @Override
    public CategoryDto update(Long catId, NewCategoryDto dto) {
        Category category = repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category not found"));



        if (!category.getName().equals(dto.getName())
                && repository.existsByName(dto.getName())) {
            throw new ConflictPropertyConstraintException("Category with this name already exists");
        }

        category.setName(dto.getName());
        return toDto(repository.save(category));
    }

    @Override
    public void delete(Long catId) {
        Category category = repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        if (!eventRepository.findAllByCategory(catId).isEmpty())
            throw new ConflictRelationsConstraintException("У категории есть события");
        repository.delete(category);
    }

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return repository.findAll(page)
                .map(CategoryMapperCustom::toDto)
                .getContent();
    }

    @Override
    public CategoryDto getById(Long catId) {
        Category category = repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        return CategoryMapperCustom.toDto(category);
    }

    @Override
    public CategoryDto getCategoryDtoById(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        return CategoryMapperCustom.toDto(category);
    }
}