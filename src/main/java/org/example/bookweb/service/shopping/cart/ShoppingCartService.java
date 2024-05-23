package org.example.bookweb.service.shopping.cart;

import org.example.bookweb.dto.shopping.cart.CartItemRequestDto;
import org.example.bookweb.dto.shopping.cart.ShoppingCartResponseDto;
import org.example.bookweb.dto.shopping.cart.UpdateCartItemDto;
import org.example.bookweb.models.ShoppingCart;
import org.example.bookweb.models.User;

public interface ShoppingCartService {
    ShoppingCart createShoppingCart(User user);

    ShoppingCartResponseDto getShoppingCart(Long userId);

    ShoppingCartResponseDto addCartItem(CartItemRequestDto cartItemRequestDto, Long userId);

    ShoppingCartResponseDto updateCartItem(Long userId,
                                           Long cartItemId,
                                           UpdateCartItemDto updateCartItemDto);

    void removeCarItemFromCart(Long userId, Long cartItemId);
}
