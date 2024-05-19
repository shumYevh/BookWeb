package org.example.bookweb.service.category;

import java.util.List;
import org.example.bookweb.dto.book.BookDto;
import org.example.bookweb.dto.category.CategoryDto;
import org.example.bookweb.dto.category.CreateCategoryRequestDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto add(CreateCategoryRequestDto requestDto);

    CategoryDto update(Long id, CreateCategoryRequestDto dto);

    void deleteById(Long id);

    CategoryDto findById(Long id);

    List<BookDto> findBooksByCategoryId(Long categoryId);

}
