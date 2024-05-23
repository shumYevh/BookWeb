package org.example.bookweb.service.shopping.cart;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.bookweb.dto.shopping.cart.AddCartItemDto;
import org.example.bookweb.dto.shopping.cart.ShoppingCartResponseDto;
import org.example.bookweb.dto.shopping.cart.UpdateCartItemDto;
import org.example.bookweb.exeption.EntityNotFoundException;
import org.example.bookweb.mapper.ShoppingCartMapper;
import org.example.bookweb.models.Book;
import org.example.bookweb.models.CartItem;
import org.example.bookweb.models.ShoppingCart;
import org.example.bookweb.repository.BookRepository;
import org.example.bookweb.repository.shopping.cart.CartItemRepository;
import org.example.bookweb.repository.shopping.cart.ShoppingCartRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCart createShoppingCart(ShoppingCart shoppingCart) {
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto getShoppingCart(String email) {
        return shoppingCartMapper.toShoppingCartResponseDto(
                shoppingCartRepository.findShoppingCartByUserEmail(email));
    }

    @Override
    public ShoppingCartResponseDto addCartItem(AddCartItemDto addCartItemDto, String email) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserEmail(email);
        Book book = bookRepository.findById(addCartItemDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with id "
                        + addCartItemDto.getBookId())
        );

        Optional<CartItem> existingCartItem = shoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getBook().equals(book))
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + addCartItemDto.getQuantity());
            cartItemRepository.save(cartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setBook(book);
            cartItem.setQuantity(addCartItemDto.getQuantity());
            cartItemRepository.save(cartItem);
            shoppingCart.getCartItems().add(cartItem);
        }
        return shoppingCartMapper
                .toShoppingCartResponseDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public ShoppingCartResponseDto updateCartItem(String email,
                                                  Long cartItemId,
                                                  UpdateCartItemDto updateCartItemDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find cartItem with id " + cartItemId)
        );
        cartItem.setQuantity(updateCartItemDto.getQuantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toShoppingCartResponseDto(
                shoppingCartRepository.findShoppingCartByUserEmail(email));
    }

    @Override
    public void removeCarItemFromCart(String email, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findCartItemById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find cart item with id " + cartItemId)
        );
        cartItemRepository.delete(cartItem);
    }
}
