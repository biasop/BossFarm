package com.bossfarm.backend.service;

import com.bossfarm.backend.dto.OrderRequest;
import com.bossfarm.backend.dto.OrderResponse;
import com.bossfarm.backend.enums.OrderStatus;
import com.bossfarm.backend.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    // Tạo đơn hàng mới (Checkout)
    OrderResponse createOrder(OrderRequest request);

    // Lấy chi tiết đơn hàng theo ID
    OrderResponse getOrderById(UUID id);

    // Lấy lịch sử đơn hàng của 1 khách hàng
    List<OrderResponse> getOrdersByUserId(UUID userId);

    // Lấy toàn bộ đơn hàng có phân trang (cho Admin)
    Page<OrderResponse> getAllOrders(Pageable pageable);

    // Lọc đơn hàng theo trạng thái xử lý
    Page<OrderResponse> getOrdersByStatus(OrderStatus status, Pageable pageable);

    // Cập nhật trạng thái đơn hàng (PENDING -> PROCESSING -> SHIPPING -> COMPLETED)
    OrderResponse updateOrderStatus(UUID id, OrderStatus status);

    // Cập nhật trạng thái thanh toán (UNPAID -> PAID -> REFUNDED)
    OrderResponse updatePaymentStatus(UUID id, PaymentStatus paymentStatus);

    // Hủy đơn hàng
    OrderResponse cancelOrder(UUID id);
}
