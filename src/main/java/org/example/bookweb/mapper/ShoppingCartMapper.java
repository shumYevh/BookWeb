package org.example.bookweb.mapper;

import org.example.bookweb.config.MapperConfig;
import org.example.bookweb.dto.shopping.cart.ShoppingCartResponseDto;
import org.example.bookweb.models.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {

    @Mapping(source = "user.id", target = "userId")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);
}
