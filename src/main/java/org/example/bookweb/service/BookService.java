package org.example.bookweb.service;

import java.util.List;
import org.example.bookweb.dto.BookDto;
import org.example.bookweb.dto.BookSearchParameters;
import org.example.bookweb.dto.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto add(CreateBookRequestDto book);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto dto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters params, Pageable pageable);
}
