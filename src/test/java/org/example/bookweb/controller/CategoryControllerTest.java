package org.example.bookweb.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.example.bookweb.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookweb.dto.category.CategoryDto;
import org.example.bookweb.dto.category.CreateCategoryRequestDto;
import org.example.bookweb.utils.TestDataUtil;
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

    @Sql(scripts = {
            "classpath:database/category/remove_test_category.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "adminUser", authorities = {"ADMIN"})
    @Test
    @DisplayName("Create new category")
    public void createCategory_validRequestDto_Success() throws Exception {
        //Given
        CreateCategoryRequestDto requestDto = TestDataUtil.getTestCreateCategoryRequestDto();
        CategoryDto expected = TestDataUtil.getTestFantasyCategoryDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        //When
        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto.class);
        //Then
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "testUser", authorities = {"USER"})
    @Test
    @DisplayName("Find all categories")
    public void findAllCategories_ValidRequest_Success() throws Exception {
        //Given
        List<CategoryDto> expected = TestDataUtil.getThreeDefaultCategoryDto();
        MvcResult result = mockMvc.perform(
                get("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
        //When
        CategoryDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), CategoryDto[].class);
        //Then
        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "testUser", authorities = {"USER"})
    @Test
    @DisplayName("Find category by Id")
    public void findCategoryById_ValidRequest_Success() throws Exception {
        //Given
        long testCategoryId = 1L;
        CategoryDto expected = TestDataUtil.getTestFantasyCategoryDto();
        MvcResult result = mockMvc.perform(
                get("/categories/" + testCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
        //When
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), CategoryDto.class);
        //Then
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
        //Given
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
        //When
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        //Then
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
        List<BookDtoWithoutCategoryIds> expected = TestDataUtil
                .getThreeDefaultBookDtoWithoutCategoryIds();
        MvcResult result = mockMvc.perform(
                get("/categories/" + testCategoryId + "/books")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
        //When
        BookDtoWithoutCategoryIds[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDtoWithoutCategoryIds[].class);
        //Then
        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @SneakyThrows
    private static void tearDown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
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
}
