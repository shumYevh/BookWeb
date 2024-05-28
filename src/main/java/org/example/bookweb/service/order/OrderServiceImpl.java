package org.example.bookweb.service.order;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.bookweb.dto.order.OrderItemResponseDto;
import org.example.bookweb.dto.order.OrderRequestDto;
import org.example.bookweb.dto.order.OrderResponseDto;
import org.example.bookweb.dto.order.OrderStatusUpdateRequest;
import org.example.bookweb.exeption.EntityNotFoundException;
import org.example.bookweb.mapper.OrderItemMapper;
import org.example.bookweb.mapper.OrderMapper;
import org.example.bookweb.models.Order;
import org.example.bookweb.models.OrderItem;
import org.example.bookweb.models.ShoppingCart;
import org.example.bookweb.models.User;
import org.example.bookweb.repository.UserRepository;
import org.example.bookweb.repository.order.OrderItemRepository;
import org.example.bookweb.repository.order.OrderRepository;
import org.example.bookweb.repository.shopping.cart.ShoppingCartRepository;
import org.example.bookweb.service.shopping.cart.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartService shoppingCartService;
    private final UserRepository userRepository;

    @Override
    public Order createNewOrder(User user, OrderRequestDto orderRequestDto) {
        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setShippingAddress(orderRequestDto.getShippingAddress());
        newOrder.setStatus(Order.Status.NEW);
        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setTotal(BigDecimal.ZERO);
        return orderRepository.save(newOrder);
    }

    @Transactional
    @Override
    public OrderResponseDto placeOrder(User user, OrderRequestDto orderRequestDto) {
        ShoppingCart userCart = shoppingCartRepository.findShoppingCartByUserId(user.getId());
        User userDb = userRepository.findById(user.getId()).get();
        Order newSavedOrder = createNewOrder(userDb, orderRequestDto);;

        Set<OrderItem> orderItems = userCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = orderItemMapper.toOrderItem(cartItem);
                    orderItem.setPrice(cartItem.getBook().getPrice()
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                    orderItem.setOrder(newSavedOrder);
                    return orderItem;
                }).collect(Collectors.toSet());
        orderItemRepository.saveAll(orderItems);

        newSavedOrder.setTotal(
                orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        newSavedOrder.setOrderItems(orderItems);

        shoppingCartRepository.deleteById(userCart.getId());
        shoppingCartService.createShoppingCart(userDb);
        return orderMapper.toDto(newSavedOrder);
    }

    @Override
    public List<OrderResponseDto> getOrdersByUser(User user) {
        return orderMapper.toDto(orderRepository.findByUserId(user.getId()));
    }

    @Override
    public OrderResponseDto updateStatusByOrderId(Long orderId,
                                                  OrderStatusUpdateRequest statusUpdateRequest) {
        Order.Status statusEnum = Order.Status.fromString(statusUpdateRequest.getStatus());

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id: " + orderId)
        );

        order.setStatus(statusEnum);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(orderRepository.getOrderById(savedOrder.getId()));
    }

    @Override
    public Set<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId, User user) {
        return orderItemMapper.toDto(
                orderItemRepository.findOrderItemsByOrderIdAndUserId(orderId, user.getId())
        );
    }

    @Override
    public OrderItemResponseDto getOrderItemByIdAndOrderId(User user,
                                                           Long orderId,
                                                           Long orderItemId) {
        return orderItemMapper.toDto(orderItemRepository.findOrderItemByIdAndOrderIdAndUserId(
                orderItemId, orderId, user.getId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find order item by id: "
                                + orderItemId + " and order id: " + orderId)
        ));
    }

    @Override
    public void deleteById(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
