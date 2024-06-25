package org.example.bookweb.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.example.bookweb.dto.book.BookDto;
import org.example.bookweb.dto.book.BookSearchParameters;
import org.example.bookweb.dto.book.CreateBookRequestDto;
import org.example.bookweb.exeption.EntityNotFoundException;
import org.example.bookweb.mapper.BookMapper;
import org.example.bookweb.models.Book;
import org.example.bookweb.repository.BookRepository;
import org.example.bookweb.repository.BookSpecificationBuilder;
import org.example.bookweb.repository.category.CategoryRepository;
import org.example.bookweb.service.book.BookServiceImpl;
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
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    private static final Long TEST_BOOK_ID = 1L;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @InjectMocks
    private BookServiceImpl bookService;

    private CreateBookRequestDto createBookRequestDto;
    private Book book;
    private Book updateBook;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        // Ініціалізація тестових даних
        createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("TestTitle");
        createBookRequestDto.setAuthor("TestAuthor");
        createBookRequestDto.setIsbn("0-2414-7761-1");
        createBookRequestDto.setPrice(BigDecimal.valueOf(40));
        createBookRequestDto.setDescription("TestDescription");
        createBookRequestDto.setCoverImage("url:TestUrl");
        createBookRequestDto.setCategoryIds(Collections.emptySet()); // Порожній Set<Long>

        book = new Book();
        book.setId(TEST_BOOK_ID);
        book.setTitle(createBookRequestDto.getTitle());
        book.setAuthor(createBookRequestDto.getAuthor());
        book.setIsbn(createBookRequestDto.getIsbn());
        book.setPrice(createBookRequestDto.getPrice());
        book.setDescription(createBookRequestDto.getDescription());
        book.setCoverImage(createBookRequestDto.getCoverImage());

        updateBook = new Book();
        updateBook.setId(TEST_BOOK_ID);
        updateBook.setTitle("TestTitle2");
        updateBook.setAuthor("TestAuthor2");
        updateBook.setIsbn("0-2414-7761-1");
        updateBook.setPrice(BigDecimal.valueOf(50));
        updateBook.setDescription("TestDescription2");
        updateBook.setCoverImage("url:TestUrl2");

        bookDto = new BookDto();
        bookDto.setId(TEST_BOOK_ID);
        bookDto.setTitle(createBookRequestDto.getTitle());
        bookDto.setAuthor(createBookRequestDto.getAuthor());
        bookDto.setIsbn(createBookRequestDto.getIsbn());
        bookDto.setPrice(createBookRequestDto.getPrice());
        bookDto.setDescription(createBookRequestDto.getDescription());
        bookDto.setCoverImage(createBookRequestDto.getCoverImage());
        bookDto.setCategoryIds(createBookRequestDto.getCategoryIds());
    }

    @Test
    @DisplayName("Add new book to DB")
    public void add_WithValidRequest_ReturnNewBookDto() {
        //Given
        when(bookMapper.toModel(any(CreateBookRequestDto.class))).thenReturn(book);
        when(categoryRepository.findCategoriesByIdIn(Collections.emptySet()))
                .thenReturn(Collections.emptySet());
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toBookDto(any(Book.class))).thenReturn(bookDto);

        //When
        BookDto actual = bookService.add(createBookRequestDto);
        BookDto expected = bookDto;

        //Then
        assertEquals(expected, actual);

        verify(bookRepository, Mockito.times(1)).save(any(Book.class));
        verify(bookMapper, Mockito.times(1)).toModel(any(CreateBookRequestDto.class));
        verify(categoryRepository, Mockito.times(1)).findCategoriesByIdIn(anySet());
        verify(bookMapper, Mockito.times(1)).toBookDto(any(Book.class));
        verifyNoMoreInteractions(bookMapper, categoryRepository, bookRepository);
    }

    @Test
    @DisplayName("Find all books")
    public void findAll_Empty_ListOfBookDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(Collections.singletonList(book));
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);
        when(bookMapper.toBookDto(anyList())).thenReturn(List.of(bookDto));

        List<BookDto> expected = List.of(bookDto);
        List<BookDto> actual = bookService.findAll(pageable);
        assertEquals(expected, actual);

        verify(bookRepository, Mockito.times(1)).findAll(any(Pageable.class));
        verify(bookMapper, Mockito.times(1)).toBookDto(anyList());
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Find book with non existing id")
    public void findById_WithNonExistingBookId_EntityNotFoundException() {
        //Given
        when(bookRepository.findBookById(TEST_BOOK_ID)).thenReturn(Optional.empty());

        //When
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(TEST_BOOK_ID)
        );

        //Then
        String expected = "Can't, find book by id: " + TEST_BOOK_ID;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(bookRepository, Mockito.times(1)).findBookById(anyLong());
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Find book by Id - valid")
    public void findById_WithValidBookId_EntityNotFoundException() {
        BookDto expected = bookDto;

        when(bookRepository.findBookById(book.getId())).thenReturn(Optional.of(book));
        when(bookMapper.toBookDto(any(Book.class))).thenReturn(expected);

        BookDto actual = bookService.findById(book.getId());
        assertEquals(expected, actual);

        verify(bookRepository, Mockito.times(1)).findBookById(anyLong());
        verify(bookMapper, Mockito.times(1)).toBookDto(any(Book.class));
        verifyNoMoreInteractions(bookMapper, bookRepository);
    }

    @Test
    @DisplayName("Update book by Id - valid")
    public void update_ExistingBook_BookDto() {
        when(bookMapper.toModel(any(CreateBookRequestDto.class))).thenReturn(book);
        when(bookRepository.findBookById(TEST_BOOK_ID)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);

        BookDto expected = bookDto;
        BookDto actual = bookService.update(TEST_BOOK_ID, createBookRequestDto);

        assertEquals(expected, actual);

        verify(bookMapper, Mockito.times(1)).toModel(any(CreateBookRequestDto.class));
        verify(bookRepository, Mockito.times(1)).findBookById(anyLong());
        verify(bookRepository, Mockito.times(1)).save(any(Book.class));
        verify(bookMapper, Mockito.times(1)).toBookDto(any(Book.class));
        verifyNoMoreInteractions(bookMapper, bookRepository);
    }

    @Test
    @DisplayName("Update book with non existing id")
    public void update_WithNonExistingBookId_EntityNotFoundException() {

        when(bookRepository.findBookById(TEST_BOOK_ID)).thenReturn(Optional.empty());
        when(bookMapper.toModel(any(CreateBookRequestDto.class))).thenReturn(book);

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class, () -> {
                    bookService.update(TEST_BOOK_ID, createBookRequestDto);
                });

        String expected = "Can't, update book by id: " + TEST_BOOK_ID;
        String actual = exception.getMessage();

        assertEquals(expected, actual);

        verify(bookRepository, Mockito.times(1)).findBookById(anyLong());
        verify(bookMapper, Mockito.times(1)).toModel(any(CreateBookRequestDto.class));
        verifyNoMoreInteractions(bookMapper, bookRepository);
    }

    @Test
    @DisplayName("Find books by search parameters")
    public void search_ValidParams_ListBookDto() {
        //Given
        String[] titles = new String[] {"TestTitle"};
        BookSearchParameters params = new BookSearchParameters(titles, null, null);
        Specification<Book> specification = Specification.where(null);
        Pageable pageable = PageRequest.of(0, 10);

        when(bookSpecificationBuilder.build(params)).thenReturn(specification);
        when(bookRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(book)));
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);
        List<BookDto> expected = List.of(bookDto);

        List<BookDto> result = bookService.search(params, pageable);

        assertEquals(expected, result);
    }
}
