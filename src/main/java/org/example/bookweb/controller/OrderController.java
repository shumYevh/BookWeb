package org.example.bookweb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.bookweb.dto.order.OrderItemResponseDto;
import org.example.bookweb.dto.order.OrderRequestDto;
import org.example.bookweb.dto.order.OrderResponseDto;
import org.example.bookweb.dto.order.OrderStatusUpdateRequest;
import org.example.bookweb.models.User;
import org.example.bookweb.service.order.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
@RequestMapping(value = "/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "",
            description = "")
    public OrderResponseDto placeOrder(Authentication authentication,
                                       @Valid @RequestBody OrderRequestDto orderRequestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.placeOrder(user, orderRequestDto);
    }

    @GetMapping
    @Operation(summary = "",
            description = "")
    public List<OrderResponseDto> getOrderHistory(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrdersByUser(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{orderId}")
    @Operation(summary = "",
            description = "")
    public OrderResponseDto updateStatus(Authentication authentication,
                                         @PathVariable Long orderId,
                                         @RequestBody OrderStatusUpdateRequest statusRequest) {
        return orderService.updateStatusByOrderId(orderId, statusRequest);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "",
            description = "")
    public Set<OrderItemResponseDto> getItemsByOrder(Authentication authentication,
                                                     @PathVariable Long orderId) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderItemsByOrderId(orderId, user);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "",
            description = "")
    public OrderItemResponseDto getItemByOrder(Authentication authentication,
                                          @PathVariable Long orderId,
                                          @PathVariable Long itemId) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderItemByIdAndOrderId(user, orderId, itemId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order by id", description = "Delete order by id (only for admin)")
    public void delete(@PathVariable Long id) {
        orderService.deleteById(id);
    }
}
