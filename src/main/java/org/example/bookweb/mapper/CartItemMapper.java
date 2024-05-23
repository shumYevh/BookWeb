package org.example.bookweb.mapper;

import org.example.bookweb.config.MapperConfig;
import org.example.bookweb.dto.shopping.cart.CartItemResponseDto;
import org.example.bookweb.models.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemResponseDto toCartItemResponseDto(CartItem cartItem);
}
