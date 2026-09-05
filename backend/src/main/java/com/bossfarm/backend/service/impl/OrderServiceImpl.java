package com.bossfarm.backend.service.impl;

import com.bossfarm.backend.dto.OrderItemRequest;
import com.bossfarm.backend.dto.OrderItemResponse;
import com.bossfarm.backend.dto.OrderRequest;
import com.bossfarm.backend.dto.OrderResponse;
import com.bossfarm.backend.enums.OrderStatus;
import com.bossfarm.backend.enums.PaymentStatus;
import com.bossfarm.backend.exception.ResourceNotFoundException;
import com.bossfarm.backend.model.Order;
import com.bossfarm.backend.model.OrderItem;
import com.bossfarm.backend.model.Product;
import com.bossfarm.backend.model.User;
import com.bossfarm.backend.repository.OrderItemRepository;
import com.bossfarm.backend.repository.OrderRepository;
import com.bossfarm.backend.repository.ProductRepository;
import com.bossfarm.backend.repository.UserRepository;
import com.bossfarm.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // Helper map từ OrderItem Entity sang OrderItemResponse DTO
    private OrderItemResponse mapItemToResponse(OrderItem item) {
        BigDecimal totalPrice = item.getTotalPrice() != null
                ? item.getTotalPrice()
                : item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

        return OrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .productThumbnailUrl(item.getProduct().getThumbnailUrl())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .totalPrice(totalPrice)
                .build();
    }

    // Helper map từ Order Entity sang OrderResponse DTO
    private OrderResponse mapOrderToResponse(Order order, List<OrderItem> items) {
        List<OrderItemResponse> itemResponses = items.stream()
                .map(this::mapItemToResponse)
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser() != null ? order.getUser().getId() : null)
                .username(order.getUser() != null ? order.getUser().getUsername() : null)
                .recipientName(order.getRecipientName())
                .phoneNumber(order.getPhoneNumber())
                .shippingAddress(order.getShippingAddress())
                .notes(order.getNotes())
                .totalAmount(order.getTotalAmount())
                .paymentStatus(order.getPaymentStatus())
                .status(order.getStatus())
                .items(itemResponses)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    // Helper load items cho 1 order
    private OrderResponse mapOrderToResponse(Order order) {
        List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
        return mapOrderToResponse(order, items);
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        // 1. Kiểm tra tài khoản người đặt (nếu có userId)
        User user = null;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));
        }

        // 2. Duyệt qua từng món hàng, kiểm tra sản phẩm và tính tổng tiền
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItemsToSave = new ArrayList<>();

        for (OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", itemReq.getProductId()));

            if (!Boolean.TRUE.equals(product.getIsActive())) {
                throw new IllegalArgumentException("Sản phẩm '" + product.getName() + "' hiện đang tạm ngưng kinh doanh!");
            }

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();

            orderItemsToSave.add(orderItem);
        }

        // 3. Tạo và lưu Order gốc
        Order order = Order.builder()
                .user(user)
                .recipientName(request.getRecipientName())
                .phoneNumber(request.getPhoneNumber())
                .shippingAddress(request.getShippingAddress())
                .notes(request.getNotes())
                .totalAmount(totalAmount)
                .paymentStatus(PaymentStatus.UNPAID)
                .status(OrderStatus.PENDING)
                .build();

        Order savedOrder = orderRepository.save(order);

        // 4. Gán Order vào từng OrderItem và lưu toàn bộ OrderItems xuống DB
        for (OrderItem item : orderItemsToSave) {
            item.setOrder(savedOrder);
        }
        List<OrderItem> savedItems = orderItemRepository.saveAll(orderItemsToSave);

        // 5. Trả về OrderResponse hoàn chỉnh
        return mapOrderToResponse(savedOrder, savedItems);
    }

    @Override
    public OrderResponse getOrderById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        return mapOrderToResponse(order);
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapOrderToResponse)
                .toList();
    }

    @Override
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(this::mapOrderToResponse);
    }

    @Override
    public Page<OrderResponse> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findByStatus(status, pageable)
                .map(this::mapOrderToResponse);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(UUID id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return mapOrderToResponse(updatedOrder);
    }

    @Override
    @Transactional
    public OrderResponse updatePaymentStatus(UUID id, PaymentStatus paymentStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        order.setPaymentStatus(paymentStatus);
        Order updatedOrder = orderRepository.save(order);
        return mapOrderToResponse(updatedOrder);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new IllegalArgumentException("Không thể hủy đơn hàng đã hoàn thành!");
        }
        if (order.getStatus() == OrderStatus.SHIPPING) {
            throw new IllegalArgumentException("Đơn hàng đang được vận chuyển, không thể tự hủy!");
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order updatedOrder = orderRepository.save(order);
        return mapOrderToResponse(updatedOrder);
    }
}
