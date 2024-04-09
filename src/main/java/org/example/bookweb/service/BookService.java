package org.example.bookweb.service;

import java.util.List;
import org.example.bookweb.dto.BookDto;
import org.example.bookweb.dto.CreateBookRequestDto;

public interface BookService {
    BookDto add(CreateBookRequestDto book);

    List<BookDto> findAll();

    BookDto findById(Long id);
}
