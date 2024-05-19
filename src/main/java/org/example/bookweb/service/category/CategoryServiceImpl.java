package org.example.bookweb.service.category;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bookweb.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookweb.dto.category.CategoryDto;
import org.example.bookweb.dto.category.CreateCategoryRequestDto;
import org.example.bookweb.exeption.EntityNotFoundException;
import org.example.bookweb.mapper.BookMapper;
import org.example.bookweb.mapper.CategoryMapper;
import org.example.bookweb.models.Category;
import org.example.bookweb.repository.BookRepository;
import org.example.bookweb.repository.category.CategoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryMapper.toDto(categoryRepository.findAll(pageable).toList());
    }

    @Override
    public CategoryDto add(CreateCategoryRequestDto requestDto) {
        Category category = categoryMapper.toModel(requestDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Transactional
    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto categoryDto) {
        Category newCategory = categoryMapper.toModel(categoryDto);
        newCategory.setId(id);
        Category existCategory = categoryRepository.findCategoryById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't, update category by id: " + id)
        );
        Category savedCategory = categoryRepository.save(newCategory);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findCategoryById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't, find book by id: " + id)
        );
        return categoryMapper.toDto(category);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findBooksByCategoryId(Long categoryId) {
        return bookMapper.toBookDtoWithoutCategoryIds(
                bookRepository.findBooksByCategoriesId(categoryId));
    }
}
