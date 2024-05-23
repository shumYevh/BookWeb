package org.example.bookweb.repository.shopping.cart;

import java.util.Optional;
import org.example.bookweb.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CartItemRepository extends
        JpaRepository<CartItem, Long>,
        JpaSpecificationExecutor<CartItem> {
    Optional<CartItem> findCartItemById(Long cartId);

    Optional<CartItem> findByIdAndShoppingCartId(Long id, Long cartId);
}
