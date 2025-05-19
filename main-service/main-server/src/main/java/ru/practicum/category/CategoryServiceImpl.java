package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    public CategoryDto create(NewCategoryDto dto) {
        if (repository.existsByName(dto.getName())) {
            throw new ConflictException("Category with name alredy exists");
        }
        Category category = mapper.toEntity(dto);
        return mapper.toDto(repository.save(category));
    }

    @Override
    public CategoryDto update(Long catId, NewCategoryDto dto) {
        Category category = repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (!category.getName().equals(dto.getName()) &&
                repository.existsByName(dto.getName())) {
            throw new ConflictException("Category with this name already exists");
        }

        category.setName(dto.getName());
        return mapper.toDto(repository.save(category));
    }

    @Override
    public void delete(Long CatId) {
        Category category = repository.findById(CatId)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        repository.delete(category);
    }

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        PageRequest page = PageRequest.of(from / size, size);
        return repository.findAll(page)
                .map(mapper::toDto)
                .getContent();
    }

    @Override
    public CategoryDto getById(Long CatId) {
        Category category = repository.findById(CatId)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        return mapper.toDto(category);
    }

    @Override
    public CategoryDto getCategoryDtoById(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}