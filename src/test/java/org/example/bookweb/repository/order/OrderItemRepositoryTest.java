package org.example.bookweb.repository.order;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

import lombok.SneakyThrows;
import org.example.bookweb.models.Order;
import org.example.bookweb.models.OrderItem;
import org.example.bookweb.models.User;
import org.example.bookweb.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderItemRepositoryTest {
    private static final String TEST_USER_EMAIL = "john.doe@example.com";

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    static void setUpBeforeClass(
            @Autowired DataSource dataSource
            ) throws Exception {
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/add_three_default_books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/user/add_john_user_to_users_table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/order/add_order_for_john_to_order_table.sql")
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
                    new ClassPathResource("database/order/remove_all_orders.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/remove_all_books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/user/remove_all_users.sql")
            );
        }
    }

    @Test
    @DisplayName("Get order items by order and user ids")
    @Sql(scripts = {
            "classpath:database/orderItem/"
                    + "add_orderItem_with_SpringBoot_book_to_order_Items_table.sql",
            "classpath:database/orderItem/"
                    + "add_orderItem_with_Java_book_to_order_Items_table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/orderItem/remove_all_order_items.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getOrderItemsByOrderIdAndUserId_correctIds_SetOrderItem() {

        Set<OrderItem> expected = new HashSet<>(orderItemRepository.findAll());
        User testUserFromDb = userRepository.findByEmail(TEST_USER_EMAIL).get();
        Order testOrderFromDb = orderRepository.findByUserId(testUserFromDb.getId()).get(0);

        Set<OrderItem> actual = orderItemRepository
                .getOrderItemsByOrderIdAndUserId(
                        testOrderFromDb.getId(),
                        testUserFromDb.getId());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find order item by order item, order and user ids")
    @Sql(scripts = {
            "classpath:database/orderItem/"
                    + "add_orderItem_with_SpringBoot_book_to_order_Items_table.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/orderItem/remove_all_order_items.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findByIdAndOrderIdAndUserId() {
        Long testItemId = 1L;
        Long testOrderId = 1L;
        Long testUserId = 2L;
        User expectedUser = userRepository.findByEmail(TEST_USER_EMAIL).orElseThrow();
        OrderItem expected = orderItemRepository.findAll().get(0);
        Order expectedOrder = orderRepository.findAll().get(0);

        OrderItem actual = orderItemRepository.findByIdAndOrderIdAndUserId(
                testItemId, testOrderId, testUserId).get();
        Order actualOrder = actual.getOrder();
        User actualUser = actualOrder.getUser();

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expectedOrder, actualOrder);
        Assertions.assertEquals(expectedUser, actualUser);
    }
}
