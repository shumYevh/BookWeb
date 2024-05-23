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
    public ShoppingCartResponseDto getShoppingCart(Long userId) {
        return shoppingCartMapper.toShoppingCartResponseDto(
                shoppingCartRepository.findShoppingCartByUserId(userId));
    }

    @Override
    public ShoppingCartResponseDto addCartItem(AddCartItemDto addCartItemDto, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(userId);
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
    public ShoppingCartResponseDto updateCartItem(Long userId,
                                                  Long cartItemId,
                                                  UpdateCartItemDto updateCartItemDto) {
        ShoppingCart cart = shoppingCartRepository.findShoppingCartByUserId(userId);
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId, cart.getId())
                .map(item -> {
                    item.setQuantity(updateCartItemDto.getQuantity());
                    return item;
                })
                .orElseThrow(() -> new EntityNotFoundException(
                                String.format("No cart item with id: %d for user: %d",
                                        cartItemId,
                                        userId)
                ));

        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toShoppingCartResponseDto(
                shoppingCartRepository.findShoppingCartByUserId(userId));
    }

    @Override
    public void removeCarItemFromCart(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findCartItemById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find cart item with id " + cartItemId)
        );
        cartItemRepository.delete(cartItem);
    }
}
