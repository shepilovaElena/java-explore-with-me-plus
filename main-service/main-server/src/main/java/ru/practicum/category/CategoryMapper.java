package ru.practicum.category;

import org.mapstruct.Mapper;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toDto(Category category);
    Category toEntity(NewCategoryDto newCategoryDto);
}
