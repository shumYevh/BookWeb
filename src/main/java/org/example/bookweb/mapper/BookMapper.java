package org.example.bookweb.mapper;

import java.util.List;
import org.example.bookweb.config.MapperConfig;
import org.example.bookweb.dto.book.BookDto;
import org.example.bookweb.dto.book.CreateBookRequestDto;
import org.example.bookweb.models.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    List<BookDto> toDto(List<Book> books);

    Book toModel(CreateBookRequestDto bookDto);
}
