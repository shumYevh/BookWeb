package org.example.bookweb.service.book;

import java.util.List;
import org.example.bookweb.dto.book.BookDto;
import org.example.bookweb.dto.book.BookSearchParameters;
import org.example.bookweb.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto add(CreateBookRequestDto book);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto dto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters params, Pageable pageable);

}
