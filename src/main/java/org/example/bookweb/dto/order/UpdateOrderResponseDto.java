package org.example.bookweb.dto.order;

import lombok.Data;

@Data
public class UpdateOrderResponseDto {
    private Long orderId;
    private String status;
    private String shippingAddress;
}
