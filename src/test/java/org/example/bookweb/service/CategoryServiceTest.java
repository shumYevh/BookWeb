package org.example.bookweb.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.example.bookweb.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookweb.dto.category.CategoryDto;
import org.example.bookweb.dto.category.CreateCategoryRequestDto;
import org.example.bookweb.exeption.EntityNotFoundException;
import org.example.bookweb.mapper.BookMapper;
import org.example.bookweb.mapper.CategoryMapper;
import org.example.bookweb.models.Book;
import org.example.bookweb.models.Category;
import org.example.bookweb.repository.BookRepository;
import org.example.bookweb.repository.category.CategoryRepository;
import org.example.bookweb.service.category.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    private static final Long TEST_CATEGORY_ID = 1L;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;
    private Category testCategory;
    private CategoryDto testCategoryDto;
    private CreateCategoryRequestDto testCreateCategoryRequestDto;

    @BeforeEach
    void setUp() {
        testCreateCategoryRequestDto = new CreateCategoryRequestDto()
                .setName("Test Category")
                .setDescription("Test Category Description");

        testCategory = new Category();
        testCategory.setId(TEST_CATEGORY_ID);
        testCategory.setName("Test Category");
        testCategory.setDescription("Test Category Description");

        testCategoryDto = new CategoryDto()
                .setId(testCategory.getId())
                .setName(testCategory.getName())
                .setDescription(testCategory.getDescription());
    }

    @Test
    @DisplayName("""
            Find all books
            """)
    public void findAll_Empty_ListOfCategoryDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(Collections.singletonList(testCategory));
        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(categoryPage);
        when(categoryMapper.toDto(anyList())).thenReturn(List.of(testCategoryDto));

        List<CategoryDto> expected = List.of(testCategoryDto);
        List<CategoryDto> actual = categoryService.findAll(pageable);
        assertEquals(expected, actual);

        verify(categoryRepository, Mockito.times(1)).findAll(any(Pageable.class));
        verify(categoryMapper, Mockito.times(1)).toDto(anyList());
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("""
            Add new valid category to DB
            """)
    public void add_WithValidRequest_ReturnNewBookDto() {
        //Given
        CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto();
        Mockito.when(categoryMapper.toModel(Mockito.any(CreateCategoryRequestDto.class)))
                .thenReturn(testCategory);
        Mockito.when(categoryRepository.save(Mockito.any(Category.class)))
                .thenReturn(testCategory);
        Mockito.when(categoryMapper.toDto(Mockito.any(Category.class)))
                .thenReturn(testCategoryDto);
        CategoryDto expected = testCategoryDto;

        //When
        CategoryDto actual = categoryService.add(createCategoryRequestDto);

        //Then
        Assertions.assertEquals(expected, actual);

        verify(categoryMapper, Mockito.times(1)).toModel(any(CreateCategoryRequestDto.class));
        verify(categoryRepository, Mockito.times(1)).save(Mockito.any(Category.class));
        verify(categoryMapper, Mockito.times(1)).toDto(Mockito.any(Category.class));
    }

    @Test
    @DisplayName("""
            Update category - valid
            """)
    public void update_ExistingCategory_CategoryDto() {
        //Given
        when(categoryMapper.toModel(any(CreateCategoryRequestDto.class)))
                .thenReturn(testCategory);
        when(categoryRepository.findCategoryById(TEST_CATEGORY_ID))
                .thenReturn(Optional.of(testCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);
        when(categoryMapper.toDto(testCategory)).thenReturn(testCategoryDto);
        CategoryDto expected = testCategoryDto;

        //When
        CategoryDto actual = categoryService.update(TEST_CATEGORY_ID, testCreateCategoryRequestDto);

        //Then
        Assertions.assertEquals(expected, actual);
        verify(categoryMapper, Mockito.times(1)).toModel(any(CreateCategoryRequestDto.class));
        verify(categoryRepository, Mockito.times(1)).findCategoryById(anyLong());
        verify(categoryRepository, Mockito.times(1)).save(Mockito.any(Category.class));
        verify(categoryMapper, Mockito.times(1)).toDto(Mockito.any(Category.class));
        verifyNoMoreInteractions(categoryMapper, categoryRepository);
    }

    @Test
    @DisplayName("""
            Update category with non existing id
            """)
    public void update_WithNonExistingCategoryId_EntityNotFoundException() {
        when(categoryMapper.toModel(any(CreateCategoryRequestDto.class)))
                .thenReturn(testCategory);
        when(categoryRepository.findCategoryById(TEST_CATEGORY_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class, () -> {
                    categoryService.update(TEST_CATEGORY_ID, testCreateCategoryRequestDto);
                });

        String expected = "Can't, update category by id: " + TEST_CATEGORY_ID;
        String actual = exception.getMessage();

        assertEquals(expected, actual);

        verify(categoryRepository, Mockito.times(1)).findCategoryById(anyLong());
        verify(categoryMapper, Mockito.times(1)).toModel(any(CreateCategoryRequestDto.class));
        verifyNoMoreInteractions(categoryMapper, categoryRepository);
    }

    @Test
    @DisplayName("""
            Find category by Id - valid
            """)
    public void findById_WithValidBookId_CategoryDto() {
        CategoryDto expected = testCategoryDto;

        when(categoryRepository.findCategoryById(testCategory.getId()))
                .thenReturn(Optional.of(testCategory));
        when(categoryMapper.toDto(any(Category.class))).thenReturn(testCategoryDto);

        CategoryDto actual = categoryService.findById(TEST_CATEGORY_ID);
        assertEquals(expected, actual);

        verify(categoryRepository, Mockito.times(1))
                .findCategoryById(anyLong());
        verify(categoryMapper, Mockito.times(1)).toDto(any(Category.class));
        verifyNoMoreInteractions(categoryMapper, categoryRepository);
    }

    @Test
    @DisplayName("""
            Find category with non existing id
            """)
    public void findById_WithNonExistingBookId_EntityNotFoundException() {
        //Given
        when(categoryRepository.findCategoryById(TEST_CATEGORY_ID)).thenReturn(Optional.empty());

        //When
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.findById(TEST_CATEGORY_ID)
        );

        //Then
        String expected = "Can't, find book by id: " + TEST_CATEGORY_ID;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(categoryRepository, Mockito.times(1)).findCategoryById(anyLong());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("""
            Find book by category id
            """)
    public void findBooksByCategoryId_ExistingBooks_ListOfBookDtoWithoutCategoryIds() {
        //Given
        Long testBookId = 2L;
        BookDtoWithoutCategoryIds testBookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds();
        testBookDtoWithoutCategoryIds.setId(testBookId);
        Book testBook = new Book();
        testBook.setId(testBookId);

        when(bookRepository.findBooksByCategoriesId(anyLong())).thenReturn(List.of(testBook));
        when(bookMapper.toBookDtoWithoutCategoryIds(anyList()))
                .thenReturn(List.of(testBookDtoWithoutCategoryIds));
        List<BookDtoWithoutCategoryIds> expected = List.of(testBookDtoWithoutCategoryIds);

        //When
        List<BookDtoWithoutCategoryIds> actual = categoryService
                .findBooksByCategoryId(testBookId);

        //Then
        Assertions.assertEquals(expected, actual);
        verify(bookRepository, Mockito.times(1)).findBooksByCategoriesId(anyLong());
        verify(bookMapper, Mockito.times(1)).toBookDtoWithoutCategoryIds(anyList());
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }
}
