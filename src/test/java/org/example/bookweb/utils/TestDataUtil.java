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
    public static CreateBookRequestDto getTestCreateBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("Test Title")
                .setAuthor("Test Author")
                .setIsbn("0-7290-0264-0")
                .setPrice(BigDecimal.valueOf(50))
                .setCoverImage("url://TestCoverImage");
    }

    public static BookDto getTestBookDto() {
        return new BookDto()
                .setTitle("Test Title")
                .setAuthor("Test Author")
                .setIsbn("0-7290-0264-0")
                .setPrice(BigDecimal.valueOf(50))
                .setCoverImage("url://TestCoverImage");
    }

    public static List<BookDto> getThreeDefaultBookDto() {
        List<BookDto> expected = new ArrayList<>();
        expected.add(new BookDto()
                .setId(1L)
                .setTitle("Mastering Java")
                .setAuthor("Jane Smith")
                .setIsbn("9789876543210")
                .setPrice(BigDecimal.valueOf(39.99))
                .setDescription("An advanced guide to Java programming")
                .setCoverImage("url://mastering_java_cover.jpg")
                .setCategoryIds(Collections.emptySet()));

        expected.add(new BookDto()
                .setId(2L)
                .setTitle("Learning Spring Boot")
                .setAuthor("John Doe")
                .setIsbn("9781234567897")
                .setPrice(BigDecimal.valueOf(29.99))
                .setDescription("A comprehensive guide to Spring Boot")
                .setCoverImage("url://spring_boot_cover.jpg")
                .setCategoryIds(Collections.emptySet()));

        expected.add(new BookDto()
                .setId(3L)
                .setTitle("Learning Hibernate")
                .setAuthor("Jack Hobbit")
                .setIsbn("9781234567895")
                .setPrice(BigDecimal.valueOf(59.99))
                .setDescription("A comprehensive guide to Hibernate")
                .setCoverImage("url://hibernate_cover.jpg")
                .setCategoryIds(Collections.emptySet()));

        return expected;
    }

    public static List<BookDtoWithoutCategoryIds> getThreeDefaultBookDtoWithoutCategoryIds() {
        List<BookDtoWithoutCategoryIds> expected = new ArrayList<>();
        expected.add(new BookDtoWithoutCategoryIds()
                .setId(1L)
                .setTitle("Mastering Java")
                .setAuthor("Jane Smith")
                .setIsbn("9789876543210")
                .setPrice(BigDecimal.valueOf(39.99))
                .setDescription("An advanced guide to Java programming")
                .setCoverImage("url://mastering_java_cover.jpg"));

        expected.add(new BookDtoWithoutCategoryIds()
                .setId(2L)
                .setTitle("Learning Spring Boot")
                .setAuthor("John Doe")
                .setIsbn("9781234567897")
                .setPrice(BigDecimal.valueOf(29.99))
                .setDescription("A comprehensive guide to Spring Boot")
                .setCoverImage("url://spring_boot_cover.jpg")
        );

        expected.add(new BookDtoWithoutCategoryIds()
                .setId(3L)
                .setTitle("Learning Hibernate")
                .setAuthor("Jack Hobbit")
                .setIsbn("9781234567895")
                .setPrice(BigDecimal.valueOf(59.99))
                .setDescription("A comprehensive guide to Hibernate")
                .setCoverImage("url://hibernate_cover.jpg")
        );

        return expected;
    }

    public static List<CategoryDto> getThreeDefaultCategoryDto() {
        List<CategoryDto> expected = new ArrayList<>();
        expected.add(new CategoryDto()

                .setId(1L)
                .setName("Fantasy")
                .setDescription("Books about fantastic worlds"));
        expected.add(new CategoryDto()
                .setId(2L)
                .setName("Horror")
                .setDescription("Very scared books"));
        expected.add(new CategoryDto()
                .setId(3L)
                .setName("Romantic")
                .setDescription("Beautiful books with love"));
        return expected;
    }

    public static CategoryDto getTestCategoryDto() {
        return new CategoryDto()
                .setId(1L)
                .setName("Fantasy")
                .setDescription("Books about fantastic worlds");
    }

    public static CreateCategoryRequestDto getTestCreateCategoryRequestDto() {
        return new CreateCategoryRequestDto()
                .setName("Fantasy")
                .setDescription("Books about fantastic worlds");
    }
}
