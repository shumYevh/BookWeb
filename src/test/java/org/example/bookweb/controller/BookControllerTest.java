package org.example.bookweb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.example.bookweb.dto.book.BookDto;
import org.example.bookweb.dto.book.CreateBookRequestDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext webApplicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/add_three_default_books.sql")
            );
        }
    }

    @AfterAll
    public static void afterAll(
            @Autowired DataSource dataSource
    ) {
        tearDown(dataSource);
    }

    @SneakyThrows
    private static void tearDown(DataSource dataSource) {
        try(Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/remove_all_books.sql")
            );
        }
    }

    @Sql(scripts = {
            "classpath:database/book/remove_testBook_from_books.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "adminUser", authorities = {"ADMIN"})
    @Test
    @DisplayName("Create new book")
    public void createBook_ValidRequestDto_Success() throws Exception {
        //Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Test Title")
                .setAuthor("Test Author")
                .setIsbn("0-7290-0264-0")
                .setPrice(BigDecimal.valueOf(50))
                .setCoverImage("url://TestCoverImage");

        BookDto expected = new BookDto()
                .setTitle("Test Title")
                .setAuthor("Test Author")
                .setIsbn("0-7290-0264-0")
                .setPrice(BigDecimal.valueOf(50))
                .setCoverImage("url://TestCoverImage");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        //When
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        //Then
        BookDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "testUser", authorities = {"USER"})
    @Test
    @DisplayName("FindAll books")
    public void findAllBooks_ValidRequest_Success() throws Exception {
        //Given
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
        //When
        MvcResult result = mockMvc.perform(
                get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(), BookDto[].class);
        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "testUser", authorities = {"USER"})
    @Test
    @DisplayName("Find book by id")
    public void findById_ValidRequest_Success() throws Exception {
        //Given
        Long bookId = 1L;
        BookDto expected = new BookDto()
                .setId(bookId)
                .setTitle("Mastering Java")
                .setAuthor("Jane Smith")
                .setIsbn("9789876543210")
                .setPrice(BigDecimal.valueOf(39.99))
                .setDescription("An advanced guide to Java programming")
                .setCoverImage("url://mastering_java_cover.jpg")
                .setCategoryIds(Collections.emptySet());

        //When
        MvcResult result = mockMvc.perform(
                get("/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(), BookDto.class);
        Assertions.assertEquals(expected, actual);
    }

    @Sql(scripts = {
            "classpath:database/book/add_testBook_to_books_table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove_testBook_from_books.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "adminUser", authorities = {"ADMIN"})
    @Test
    @DisplayName("Update existing book")
    public void put_validRequestDto_Success() throws Exception {
        long bookId = 99L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("NEW Test Title")
                .setAuthor("NEW Test Author")
                .setIsbn("0-7290-0264-0")
                .setPrice(BigDecimal.valueOf(100))
                .setCoverImage("url://NEW_TestCoverImage");

        BookDto expected = new BookDto()
                .setTitle("NEW Test Title")
                .setAuthor("NEW Test Author")
                .setIsbn("0-7290-0264-0")
                .setPrice(BigDecimal.valueOf(100))
                .setCoverImage("url://NEW_TestCoverImage");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                put("/books/" + bookId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Sql(scripts = {
            "classpath:database/book/add_testBook_to_books_table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove_testBook_from_books.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "adminUser", authorities = {"ADMIN"})
    @Test
    @DisplayName("Delete existing book")
    public void delete_anyRequest_Success() throws Exception {
        long bookId = 99L;
        MvcResult result = mockMvc.perform(
                        delete("/books/" + bookId)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNoContent())
                .andReturn();
    }

    @Sql(scripts = {
            "classpath:database/book/add_testBook_to_books_table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove_testBook_from_books.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "adminUser", authorities = {"ADMIN"})
    @Test
    @DisplayName("Search existing book")
    public void search_validRequest_Success() throws Exception {
        //Given
        String testBookTitle = "Test Title";
        BookDto expectedBook = new BookDto()
                .setId(99L)
                .setTitle(testBookTitle)
                .setAuthor("Test Author")
                .setIsbn("0-7290-0264-0")
                .setPrice(BigDecimal.valueOf(50))
                .setCoverImage("url://TestCoverImage");
        BookDto[] expected = new BookDto[] {expectedBook};

        MvcResult result = mockMvc.perform(
                        get("/books/search")
                                .param("titles", testBookTitle)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), BookDto[].class);

        EqualsBuilder.reflectionEquals(expected, actual);
    }
}
