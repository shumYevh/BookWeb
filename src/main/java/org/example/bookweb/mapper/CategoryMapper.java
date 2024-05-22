package org.example.bookweb.mapper;

import java.util.List;
import org.example.bookweb.config.MapperConfig;
import org.example.bookweb.dto.category.CategoryDto;
import org.example.bookweb.dto.category.CreateCategoryRequestDto;
import org.example.bookweb.models.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    List<CategoryDto> toDto(List<Category> categories);

    Category toModel(CreateCategoryRequestDto categoryDto);
}
