package org.example.bookweb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.example.bookweb.dto.book.BookDto;
import org.example.bookweb.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookweb.dto.category.CategoryDto;
import org.example.bookweb.dto.category.CreateCategoryRequestDto;
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
import java.util.List;
import java.util.Set;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
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
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/category/add_three_default_categories.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/category/add_ids_to_books_categories_table.sql")
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
                    new ClassPathResource("database/category/clear_books_categories_table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/category/remove_all_categories.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/remove_all_books.sql")
            );
        }
    }

    @Sql(scripts = {
            "classpath:database/category/remove_test_category.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "adminUser", authorities = {"ADMIN"})
    @Test
    @DisplayName("Create new category")
    public void createCategory_validRequestDto_Success() throws Exception {
        //Given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto()
                .setName("Test Category")
                .setDescription("Test Description");

        CategoryDto expected = new CategoryDto()
                .setName("Test Category")
                .setDescription("Test Description");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        //When
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        //Then
        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "testUser", authorities = {"USER"})
    @Test
    @DisplayName("Find all categories")
    public void findAllCategories_ValidRequest_Success() throws Exception {
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

        MvcResult result = mockMvc.perform(
                get("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        CategoryDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), CategoryDto[].class);
        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "testUser", authorities = {"USER"})
    @Test
    @DisplayName("Find category by Id")
    public void findCategoryById_ValidRequest_Success() throws Exception {
        //Given
        long testCategoryId = 1L;
        CategoryDto expected = new CategoryDto()
                .setId(testCategoryId)
                .setName("Fantasy")
                .setDescription("Books about fantastic worlds");
        //When
        MvcResult result = mockMvc.perform(
                get("/categories/" + testCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
        //Then
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), CategoryDto.class);
        Assertions.assertEquals(expected, actual);
    }

    @Sql(scripts = {
            "classpath:database/category/add_test_category.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/category/remove_test_category.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "testAdmin", authorities = {"ADMIN"})
    @Test
    @DisplayName("Update existing category")
    public void put_validRequestDto_Success() throws Exception {
        long testCategoryId = 99L;
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto()
                .setName("NEW Test Name")
                .setDescription("NEW Test Name");

        CategoryDto expected = new CategoryDto()
                .setId(testCategoryId)
                .setName("NEW Test Name")
                .setDescription("NEW Test Name");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                put("/categories/" + testCategoryId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Sql(scripts = {
            "classpath:database/category/add_test_category.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/category/remove_test_category.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "adminUser", authorities = {"ADMIN"})
    @Test
    @DisplayName("Delete existing category")
    public void delete_anyRequest_Success() throws Exception {
        long categoryId = 99L;
        MvcResult result = mockMvc.perform(
                        delete("/categories/" + categoryId)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNoContent())
                .andReturn();
    }

    @WithMockUser(username = "testUser", authorities = "USER")
    @Test
    @DisplayName("Find books by category id")
    public void findBooksByCategory_validRequest_Success() throws Exception {
        //Given
        long testCategoryId = 1L;
        Set<Long> testCategoryIds = Set.of(testCategoryId);
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
        //When
        MvcResult result = mockMvc.perform(
                get("/categories/" + testCategoryId + "/books")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
        //Then
        BookDtoWithoutCategoryIds[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDtoWithoutCategoryIds[].class);
        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }
}
