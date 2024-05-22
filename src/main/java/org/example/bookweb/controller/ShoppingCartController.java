package org.example.bookweb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookweb.dto.shopping.cart.AddCartItemDto;
import org.example.bookweb.dto.shopping.cart.ShoppingCartResponseDto;
import org.example.bookweb.dto.shopping.cart.UpdateCartItemDto;
import org.example.bookweb.service.shopping.cart.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management",
        description = "Endpoints for managing shopping cart with cart items")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Get Shopping Cart",
            description = "Get Shopping Cart with Cart Items")
    public ShoppingCartResponseDto getShoppingCart() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return shoppingCartService.getShoppingCart(userName);
    }

    @PostMapping
    @Operation(summary = "Add Cart Item to Shopping cart",
            description = "Add Cart Item with dto to personal Shopping Cart")
    public ShoppingCartResponseDto addCartItem(@RequestBody @Valid AddCartItemDto addCartItemDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return shoppingCartService.addCartItem(addCartItemDto, email);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update Cart Item",
            description = "Update Cart Item from Shopping Cart")
    public ShoppingCartResponseDto updateCartItem(@PathVariable Long cartItemId,
                                       @RequestBody @Valid UpdateCartItemDto updateCartItemDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return shoppingCartService.updateCartItem(email, cartItemId, updateCartItemDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Delete Cart Item",
            description = "Delete Cart Item from Shopping Cart")
    public void deleteCartItem(@PathVariable Long cartItemId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        shoppingCartService.removeCarItemFromCart(email, cartItemId);
    }

}
