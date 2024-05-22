package org.example.bookweb.repository.shopping.cart;

import org.example.bookweb.models.ShoppingCart;
import org.example.bookweb.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends
        JpaRepository<ShoppingCart, Long>,
        JpaSpecificationExecutor<ShoppingCart> {

    @Query("select cart from ShoppingCart cart left join fetch cart.cartItems")
    ShoppingCart findByUser(User userId);

}
