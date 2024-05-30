package org.example.bookweb.repository.order;

import java.util.List;
import org.example.bookweb.models.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderRepository extends
        JpaRepository<Order, Long>,
        JpaSpecificationExecutor<Order> {

    @EntityGraph(attributePaths = {"orderItems"})
    List<Order> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"orderItems", "user"})
    Order getOrderById(Long orderId);
}
