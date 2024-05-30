package org.example.bookweb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.bookweb.dto.order.OrderItemResponseDto;
import org.example.bookweb.dto.order.OrderRequestDto;
import org.example.bookweb.dto.order.OrderResponseDto;
import org.example.bookweb.dto.order.OrderStatusUpdateRequest;
import org.example.bookweb.dto.order.UpdateOrderResponseDto;
import org.example.bookweb.models.User;
import org.example.bookweb.service.order.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Place Order",
            description = "Place order from user's shopping cart")
    public OrderResponseDto placeOrder(Authentication authentication,
                                       @Valid @RequestBody OrderRequestDto orderRequestDto) {
        User user = getUserPrincipal(authentication);
        return orderService.placeOrder(user, orderRequestDto);
    }

    @GetMapping
    @Operation(summary = "Get order history",
            description = "Get user's order history")
    public List<OrderResponseDto> getOrderHistory(Authentication authentication) {
        User user = getUserPrincipal(authentication);
        return orderService.getOrdersByUser(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{orderId}")
    @Operation(summary = "Update order status",
            description = "Update order status(only for admin)")
    public UpdateOrderResponseDto updateStatus(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderStatusUpdateRequest statusRequest) {
        return orderService.updateStatusByOrderId(orderId, statusRequest);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get order Items",
            description = "Get order items from specific order and user")
    public Set<OrderItemResponseDto> getItemsByOrder(Authentication authentication,
                                                     @PathVariable @Positive Long orderId) {
        User user = getUserPrincipal(authentication);
        return orderService.getOrderItemsByOrderId(orderId, user);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get order Item",
            description = "Get order item from specific order and user")
    public OrderItemResponseDto getItemByOrder(Authentication authentication,
                                          @PathVariable @Positive Long orderId,
                                          @PathVariable @Positive Long itemId) {
        User user = getUserPrincipal(authentication);
        return orderService.getOrderItemByIdAndOrderId(user, orderId, itemId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order by id",
            description = "Delete order by id (only for admin)")
    public void delete(@PathVariable @Positive Long id) {
        orderService.deleteById(id);
    }

    private User getUserPrincipal(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
