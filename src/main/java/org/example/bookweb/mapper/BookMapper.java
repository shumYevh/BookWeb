package org.example.bookweb.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.example.bookweb.config.MapperConfig;
import org.example.bookweb.dto.book.BookDto;
import org.example.bookweb.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookweb.dto.book.CreateBookRequestDto;
import org.example.bookweb.models.Book;
import org.example.bookweb.models.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toBookDto(Book book);

    List<BookDto> toBookDto(List<Book> books);

    BookDtoWithoutCategoryIds toBookDtoWithoutCategoryIds(Book book);

    List<BookDtoWithoutCategoryIds> toBookDtoWithoutCategoryIds(List<Book> book);

    Book toModel(CreateBookRequestDto bookDto);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            bookDto.setCategoryIds(book.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet()));
        }
    }
}
