package org.example.bookweb.mapper;

import java.util.List;
import org.example.bookweb.config.MapperConfig;
import org.example.bookweb.dto.order.OrderResponseDto;
import org.example.bookweb.models.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    OrderResponseDto toDto(Order order);

    List<OrderResponseDto> toDto(List<Order> orders);
}
