package org.example.bookweb.service.book;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.bookweb.dto.book.BookDto;
import org.example.bookweb.dto.book.BookSearchParameters;
import org.example.bookweb.dto.book.CreateBookRequestDto;
import org.example.bookweb.exeption.EntityNotFoundException;
import org.example.bookweb.mapper.BookMapper;
import org.example.bookweb.models.Book;
import org.example.bookweb.models.Category;
import org.example.bookweb.repository.BookRepository;
import org.example.bookweb.repository.BookSpecificationBuilder;
import org.example.bookweb.repository.category.CategoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto add(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        Set<Category> categoriesFromDb = categoryRepository
                .findCategoriesByIdIn(requestDto.getCategoryIds());
        book.setCategories(categoriesFromDb);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toBookDto(savedBook);
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookMapper.toBookDto(bookRepository.findAll(pageable).toList());
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findBookById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't, find book by id: " + id)
        );
        return bookMapper.toBookDto(book);
    }

    @Transactional
    @Override
    public BookDto update(Long id, CreateBookRequestDto dto) {
        Book newBook = bookMapper.toModel(dto);
        newBook.setId(id);
        Book existBook = bookRepository.findBookById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't, update book by id: " + id)
        );
        newBook.setIsbn(existBook.getIsbn());
        Book savedBook = bookRepository.save(newBook);
        return bookMapper.toBookDto(savedBook);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParameters params, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(bookSpecification, pageable)
                .stream()
                .map(bookMapper::toBookDto)
                .toList();
    }
}
