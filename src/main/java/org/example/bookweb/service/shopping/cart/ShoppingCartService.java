package org.example.bookweb.service.shopping.cart;

import org.example.bookweb.dto.shopping.cart.AddCartItemDto;
import org.example.bookweb.dto.shopping.cart.ShoppingCartResponseDto;
import org.example.bookweb.dto.shopping.cart.UpdateCartItemDto;
import org.example.bookweb.models.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart createShoppingCart(ShoppingCart shoppingCart);

    ShoppingCartResponseDto getShoppingCart(String email);

    ShoppingCartResponseDto addCartItem(AddCartItemDto addCartItemDto, String email);

    ShoppingCartResponseDto updateCartItem(String email,
                                           Long cartItemId,
                                           UpdateCartItemDto updateCartItemDto);

    void removeCarItemFromCart(String email, Long cartItemId);
}
