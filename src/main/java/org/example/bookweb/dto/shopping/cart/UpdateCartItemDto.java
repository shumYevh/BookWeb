package org.example.bookweb.dto.shopping.cart;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateCartItemDto {
    @Positive
    private int quantity;
}
