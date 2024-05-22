package org.example.bookweb.mapper;

import org.example.bookweb.config.MapperConfig;
import org.example.bookweb.dto.shopping.cart.CartItemResponseDto;
import org.example.bookweb.dto.shopping.cart.ShoppingCartResponseDto;
import org.example.bookweb.models.CartItem;
import org.example.bookweb.models.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cartItems", target = "cartItems")
    ShoppingCartResponseDto toShoppingCartResponseDto(ShoppingCart shoppingCart);

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemResponseDto toCartItemResponseDto(CartItem cartItem);
}
