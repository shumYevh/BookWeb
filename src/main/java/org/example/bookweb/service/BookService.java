package org.example.bookweb.service;

import java.util.List;
import org.example.bookweb.dto.BookDto;
import org.example.bookweb.dto.BookSearchParameters;
import org.example.bookweb.dto.CreateBookRequestDto;

public interface BookService {
    BookDto add(CreateBookRequestDto book);

    List<BookDto> findAll();

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto dto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters params);
}
