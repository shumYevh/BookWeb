package org.example.bookweb.repository.shopping.cart;

import org.example.bookweb.models.ShoppingCart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends
        JpaRepository<ShoppingCart, Long>,
        JpaSpecificationExecutor<ShoppingCart> {

    @EntityGraph(attributePaths = {"cartItems", "cartItems.book"})
    ShoppingCart findShoppingCartByUserId(Long id);
}
