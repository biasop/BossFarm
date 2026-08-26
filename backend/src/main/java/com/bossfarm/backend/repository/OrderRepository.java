package com.bossfarm.backend.repository;

import com.bossfarm.backend.enums.OrderStatus;
import com.bossfarm.backend.enums.PaymentStatus;
import com.bossfarm.backend.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    // Lấy lịch sử đơn hàng của 1 khách hàng (sắp xếp mới nhất lên đầu)
    List<Order> findByUserIdOrderByCreatedAtDesc(UUID userId);

    // Lọc danh sách đơn hàng theo trạng thái (Pending, Shipping,...) có phân trang
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    // Tìm các đơn hàng theo trạng thái thanh toán (Unpaid, Paid,...)
    List<Order> findByPaymentStatus(PaymentStatus paymentStatus);
}
