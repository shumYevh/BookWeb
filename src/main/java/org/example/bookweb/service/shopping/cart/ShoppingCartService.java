package org.example.bookweb.service.shopping.cart;

import org.example.bookweb.dto.shopping.cart.AddCartItemDto;
import org.example.bookweb.dto.shopping.cart.ShoppingCartResponseDto;
import org.example.bookweb.dto.shopping.cart.UpdateCartItemDto;
import org.example.bookweb.models.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart createShoppingCart(ShoppingCart shoppingCart);

    ShoppingCartResponseDto getShoppingCart(Long userId);

    ShoppingCartResponseDto addCartItem(AddCartItemDto addCartItemDto, Long userId);

    ShoppingCartResponseDto updateCartItem(Long userId,
                                           Long cartItemId,
                                           UpdateCartItemDto updateCartItemDto);

    void removeCarItemFromCart(Long cartItemId);
}
