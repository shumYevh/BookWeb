package org.example.bookweb.repository;

import java.util.List;
import java.util.Optional;
import org.example.bookweb.models.Book;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findBookById(Long id);
}
