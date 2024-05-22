package org.example.bookweb.dto.shopping.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddCartItemDto {
    @NotNull
    private Long bookId;
    @Positive
    private int quantity;
}
