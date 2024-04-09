package org.example.bookweb.mapper;

import org.example.bookweb.config.MapperConfig;
import org.example.bookweb.dto.BookDto;
import org.example.bookweb.dto.CreateBookRequestDto;
import org.example.bookweb.models.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto bookDto);
}
