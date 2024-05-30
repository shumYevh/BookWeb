package org.example.bookweb.service.order;

import java.util.List;
import java.util.Set;
import org.example.bookweb.dto.order.OrderItemResponseDto;
import org.example.bookweb.dto.order.OrderRequestDto;
import org.example.bookweb.dto.order.OrderResponseDto;
import org.example.bookweb.dto.order.OrderStatusUpdateRequest;
import org.example.bookweb.dto.order.UpdateOrderResponseDto;
import org.example.bookweb.models.Order;
import org.example.bookweb.models.User;

public interface OrderService {

    Order createNewOrder(User user, OrderRequestDto orderRequestDto);

    OrderResponseDto placeOrder(User user, OrderRequestDto orderRequestDto);

    List<OrderResponseDto> getOrdersByUser(User user);

    UpdateOrderResponseDto updateStatusByOrderId(Long id,
                                                 OrderStatusUpdateRequest statusUpdateRequest);

    Set<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId, User user);

    OrderItemResponseDto getOrderItemByIdAndOrderId(User user, Long orderId, Long orderItemId);

    void deleteById(Long orderId);
}
