package org.example.bookweb.repository.order;

import java.util.Optional;
import java.util.Set;
import org.example.bookweb.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends
        JpaRepository<OrderItem, Long>,
        JpaSpecificationExecutor<OrderItem> {

    @Query("SELECT items FROM OrderItem items "
            + "JOIN FETCH items.order ord "
            + "JOIN FETCH ord.user usr "
            + "WHERE ord.id = :orderId AND usr.id = :userId")
    Set<OrderItem> findOrderItemsByOrderIdAndUserId(
            @Param("orderId") Long orderId,
            @Param("userId") Long userId
    );

    @Query("SELECT item FROM OrderItem item "
            + "JOIN FETCH item.order ord "
            + "JOIN FETCH ord.user usr "
            + "WHERE ord.id = :orderId AND usr.id = :userId AND item.id = :itemId")
    Optional<OrderItem> findOrderItemByIdAndOrderIdAndUserId(
            @Param("itemId") Long itemId,
            @Param("orderId") Long orderId,
            @Param("userId") Long userId
    );
}
