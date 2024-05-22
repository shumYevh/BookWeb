package org.example.bookweb.repository;

import java.util.List;
import java.util.Optional;
import org.example.bookweb.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends
        JpaRepository<Book,Long>,
        JpaSpecificationExecutor<Book> {
    Optional<Book> findBookById(Long id);

    List<Book> findBooksByCategoriesId(Long id);
}
