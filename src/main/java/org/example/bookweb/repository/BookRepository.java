package org.example.bookweb.repository;

import java.util.Optional;
import org.example.bookweb.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    Optional<Book> findBookById(Long id);
}
