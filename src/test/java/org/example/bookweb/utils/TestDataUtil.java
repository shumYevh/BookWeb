package org.example.bookweb.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.example.bookweb.dto.book.BookDto;
import org.example.bookweb.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookweb.dto.book.CreateBookRequestDto;
import org.example.bookweb.dto.category.CategoryDto;
import org.example.bookweb.dto.category.CreateCategoryRequestDto;

public class TestDataUtil {
    private static final CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto()
            .setTitle("Test Title")
            .setAuthor("Test Author")
            .setIsbn("0-7290-0264-0")
            .setPrice(BigDecimal.valueOf(50))
            .setCoverImage("url://TestCoverImage");
    private static final BookDto bookDto = new BookDto()
            .setTitle("Test Title")
            .setAuthor("Test Author")
            .setIsbn("0-7290-0264-0")
            .setPrice(BigDecimal.valueOf(50))
            .setCoverImage("url://TestCoverImage");
    private static final BookDto javaBookDto = new BookDto()
            .setId(1L)
            .setTitle("Mastering Java")
            .setAuthor("Jane Smith")
            .setIsbn("9789876543210")
            .setPrice(BigDecimal.valueOf(39.99))
            .setDescription("An advanced guide to Java programming")
            .setCoverImage("url://mastering_java_cover.jpg")
            .setCategoryIds(Collections.emptySet());
    private static final BookDto springBookDto = new BookDto()
            .setId(2L)
            .setTitle("Learning Spring Boot")
            .setAuthor("John Doe")
            .setIsbn("9781234567897")
            .setPrice(BigDecimal.valueOf(29.99))
            .setDescription("A comprehensive guide to Spring Boot")
            .setCoverImage("url://spring_boot_cover.jpg")
            .setCategoryIds(Collections.emptySet());
    private static final BookDto hibernateBookDto = new BookDto()
            .setId(3L)
            .setTitle("Learning Hibernate")
            .setAuthor("Jack Hobbit")
            .setIsbn("9781234567895")
            .setPrice(BigDecimal.valueOf(59.99))
            .setDescription("A comprehensive guide to Hibernate")
            .setCoverImage("url://hibernate_cover.jpg")
            .setCategoryIds(Collections.emptySet());
    private static final CategoryDto fantasyCategoryDto = new CategoryDto()
            .setId(1L)
            .setName("Fantasy")
            .setDescription("Books about fantastic worlds");
    private static final CategoryDto horrorCategoryDto = new CategoryDto()
            .setId(2L)
            .setName("Horror")
            .setDescription("Very scared books");
    private static final CategoryDto romanticCategoryDto = new CategoryDto()
            .setId(3L)
            .setName("Romantic")
            .setDescription("Beautiful books with love");

    public static CreateBookRequestDto getTestCreateBookRequestDto() {
        return createBookRequestDto;
    }

    public static BookDto getTestBookDto() {
        return bookDto;
    }

    public static List<BookDto> getThreeDefaultBookDto() {
        List<BookDto> expected = new ArrayList<>();
        expected.add(javaBookDto);
        expected.add(springBookDto);
        expected.add(hibernateBookDto);
        return expected;
    }

    public static List<BookDtoWithoutCategoryIds> getThreeDefaultBookDtoWithoutCategoryIds() {
        List<BookDtoWithoutCategoryIds> expected = new ArrayList<>();
        expected.add(new BookDtoWithoutCategoryIds()
                .setId(javaBookDto.getId())
                .setTitle(javaBookDto.getTitle())
                .setAuthor(javaBookDto.getAuthor())
                .setIsbn(javaBookDto.getIsbn())
                .setPrice(javaBookDto.getPrice())
                .setDescription(javaBookDto.getDescription())
                .setCoverImage(javaBookDto.getCoverImage()));

        expected.add(new BookDtoWithoutCategoryIds()
                .setId(springBookDto.getId())
                .setTitle(springBookDto.getTitle())
                .setAuthor(springBookDto.getAuthor())
                .setIsbn(springBookDto.getIsbn())
                .setPrice(springBookDto.getPrice())
                .setDescription(springBookDto.getDescription())
                .setCoverImage(springBookDto.getCoverImage()));

        expected.add(new BookDtoWithoutCategoryIds()
                .setId(hibernateBookDto.getId())
                .setTitle(hibernateBookDto.getTitle())
                .setAuthor(hibernateBookDto.getAuthor())
                .setIsbn(hibernateBookDto.getIsbn())
                .setPrice(hibernateBookDto.getPrice())
                .setDescription(hibernateBookDto.getDescription())
                .setCoverImage(hibernateBookDto.getCoverImage()));
        return expected;
    }

    public static List<CategoryDto> getThreeDefaultCategoryDto() {
        List<CategoryDto> expected = new ArrayList<>();
        expected.add(fantasyCategoryDto);
        expected.add(horrorCategoryDto);
        expected.add(romanticCategoryDto);
        return expected;
    }

    public static CategoryDto getTestFantasyCategoryDto() {
        return fantasyCategoryDto;
    }

    public static CreateCategoryRequestDto getTestCreateCategoryRequestDto() {
        return new CreateCategoryRequestDto()
                .setName(fantasyCategoryDto.getName())
                .setDescription(fantasyCategoryDto.getDescription());
    }
}
