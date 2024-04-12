package org.example.bookweb.service;

import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.example.bookweb.dto.BookDto;
import org.example.bookweb.dto.CreateBookRequestDto;
import org.example.bookweb.exeption.EntityNotFoundException;
import org.example.bookweb.mapper.BookMapper;
import org.example.bookweb.models.Book;
import org.example.bookweb.repository.BookRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto add(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        book.setIsbn(new Random().nextLong(1000000000));
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findBookById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't, find book by id: " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto dto) {
        Book newBook = bookMapper.toModel(dto);
        newBook.setId(id);
        Book existBook = bookRepository.findBookById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't, update book by id: " + id)
        );
        newBook.setIsbn(existBook.getIsbn());
        Book savedBook = bookRepository.save(newBook);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
