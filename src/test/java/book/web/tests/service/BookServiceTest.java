package book.web.tests.service;

import org.example.bookweb.dto.book.BookDto;
import org.example.bookweb.dto.book.CreateBookRequestDto;
import org.example.bookweb.exeption.EntityNotFoundException;
import org.example.bookweb.mapper.BookMapper;
import org.example.bookweb.models.Book;
import org.example.bookweb.repository.BookRepository;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    private static final Long TEST_BOOK_ID = 1L;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapper bookMapper;
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
    @DisplayName("""
            Add new book to DB
            """)
    public void add_WithValidRequest_ReturnNewBookDto() {
        //Given
        Mockito.when(bookMapper.toModel(any(CreateBookRequestDto.class))).thenReturn(book);
        Mockito.when(categoryRepository.findCategoriesByIdIn(Collections.emptySet()))
                .thenReturn(Collections.emptySet());
        Mockito.when(bookRepository.save(any(Book.class))).thenReturn(book);
        Mockito.when(bookMapper.toBookDto(any(Book.class))).thenReturn(bookDto);

        //When
        BookDto actual = bookService.add(createBookRequestDto);
        BookDto expected = bookDto;

        //Then
        Assertions.assertEquals(expected, actual);

        Mockito.verify(bookRepository, Mockito.times(1)).save(any(Book.class));
        Mockito.verify(bookMapper, Mockito.times(1)).toModel(any(CreateBookRequestDto.class));
        Mockito.verify(categoryRepository, Mockito.times(1)).findCategoriesByIdIn(anySet());
        Mockito.verify(bookMapper, Mockito.times(1)).toBookDto(any(Book.class));
        Mockito.verifyNoMoreInteractions(bookMapper, categoryRepository, bookRepository);
    }

    @Test
    @DisplayName("""
            
            """)
    public void findAll_Empty_ListOfBookDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(Collections.singletonList(book));
        Mockito.when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);
        Mockito.when(bookMapper.toBookDto(anyList())).thenReturn(List.of(bookDto));

        List<BookDto> expected = List.of(bookDto);
        List<BookDto> actual = bookService.findAll(pageable);
        Assertions.assertEquals(expected, actual);

        Mockito.verify(bookRepository, Mockito.times(1)).findAll(any(Pageable.class));
        Mockito.verify(bookMapper, Mockito.times(1)).toBookDto(anyList());
        Mockito.verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("""
            Find book with non existing id
            """)
    public void findById_WithNonExistingBookId_EntityNotFoundException() {
        //Given
        Mockito.when(bookRepository.findBookById(TEST_BOOK_ID)).thenReturn(Optional.empty());

        //When
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(TEST_BOOK_ID)
        );

        //Then
        String expected = "Can't, find book by id: " + TEST_BOOK_ID;
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);

        Mockito.verify(bookRepository, Mockito.times(1)).findBookById(anyLong());
        Mockito.verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("""
            Find book by Id - valid
            """)
    public void findById_WithValidBookId_EntityNotFoundException() {
        BookDto expected = bookDto;

        Mockito.when(bookRepository.findBookById(book.getId())).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.toBookDto(any(Book.class))).thenReturn(expected);

        BookDto actual = bookService.findById(book.getId());
        Assertions.assertEquals(expected, actual);

        Mockito.verify(bookRepository, Mockito.times(1)).findBookById(anyLong());
        Mockito.verify(bookMapper, Mockito.times(1)).toBookDto(any(Book.class));
        Mockito.verifyNoMoreInteractions(bookMapper, bookRepository);
    }

    @Test
    @DisplayName("""
            Update book by Id - valid
            """)
    public void update_ExistingBook_BookDto() {
        Mockito.when(bookMapper.toModel(any(CreateBookRequestDto.class))).thenReturn(book);
        Mockito.when(bookRepository.findBookById(TEST_BOOK_ID)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.save(any(Book.class))).thenReturn(book);
        Mockito.when(bookMapper.toBookDto(book)).thenReturn(bookDto);

        BookDto expected = bookDto;
        BookDto actual = bookService.update(TEST_BOOK_ID, createBookRequestDto);

        Assertions.assertEquals(expected, actual);

        Mockito.verify(bookMapper, Mockito.times(1)).toModel(any(CreateBookRequestDto.class));
        Mockito.verify(bookRepository, Mockito.times(1)).findBookById(anyLong());
        Mockito.verify(bookRepository, Mockito.times(1)).save(any(Book.class));
        Mockito.verify(bookMapper, Mockito.times(1)).toBookDto(any(Book.class));
        Mockito.verifyNoMoreInteractions(bookMapper, bookRepository);
    }

    @Test
    @DisplayName("""
            Update book with non existing id
            """)
    public void update_WithNonExistingBookId_EntityNotFoundException() {

        Mockito.when(bookRepository.findBookById(TEST_BOOK_ID)).thenReturn(Optional.empty());
        Mockito.when(bookMapper.toModel(any(CreateBookRequestDto.class))).thenReturn(book);

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            bookService.update(TEST_BOOK_ID, createBookRequestDto);
        });

        String expected = "Can't, update book by id: " + TEST_BOOK_ID;
        String actual = exception.getMessage();


        Assertions.assertEquals(expected, actual);

        Mockito.verify(bookRepository, Mockito.times(1)).findBookById(anyLong());
        Mockito.verify(bookMapper, Mockito.times(1)).toModel(any(CreateBookRequestDto.class));
        Mockito.verifyNoMoreInteractions(bookMapper, bookRepository);
    }
}
