package org.example.bookweb.mapper;

import java.util.List;
import java.util.Set;
import org.example.bookweb.config.MapperConfig;
import org.example.bookweb.dto.order.OrderItemResponseDto;
import org.example.bookweb.models.CartItem;
import org.example.bookweb.models.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {

    @Mapping(target = "id", ignore = true)
    OrderItem toOrderItem(CartItem cartItem);

    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toDto(OrderItem orderItem);

    List<OrderItemResponseDto> toDto(List<OrderItem> orderItems);

    Set<OrderItemResponseDto> toDto(Set<OrderItem> orderItems);
}
