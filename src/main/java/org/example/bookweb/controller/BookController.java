package org.example.bookweb.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bookweb.dto.BookDto;
import org.example.bookweb.dto.CreateBookRequestDto;
import org.example.bookweb.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<BookDto> findAll() {
        return bookService.findAll();
    }

    @PostMapping
    public BookDto save(@RequestBody CreateBookRequestDto dto) {
        return bookService.add(dto);
    }

    @GetMapping("/{id}")
    public BookDto findById(@PathVariable Long id) {
        return bookService.findById(id);
    }
}
