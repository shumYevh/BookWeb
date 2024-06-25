package org.example.bookweb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bookweb.dto.book.BookDto;
import org.example.bookweb.dto.book.BookSearchParameters;
import org.example.bookweb.dto.book.CreateBookRequestDto;
import org.example.bookweb.service.book.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
@Validated
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books with pageable params",
            description = "Get all books with pageable parameters for registered users"
    )
    public List<BookDto> findAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save book with dto", description = "Save book with dto (only for admin)")
    public BookDto save(@RequestBody @Valid CreateBookRequestDto dto) {
        return bookService.add(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find book by id", description = "Find book by id for registered users")
    public BookDto findById(@PathVariable @Positive Long id) {
        return bookService.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update book by id", description = "Update book by id (only for admin)")
    public BookDto put(@PathVariable Long id, @RequestBody @Valid CreateBookRequestDto dto) {
        return bookService.update(id, dto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book by id", description = "Delete book by id (only for admin)")
    public void delete(@PathVariable @Positive Long id) {
        bookService.deleteById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search books by parameters with pageable",
            description = "Search books by title, authors, "
                    + "isbns and pageable parameters for registered users"
    )
    public List<BookDto> search(Pageable pageable, BookSearchParameters searchParameters) {
        return bookService.search(searchParameters, pageable);
    }
}
