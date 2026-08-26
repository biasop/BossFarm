package com.bossfarm.backend.repository;

import com.bossfarm.backend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    // Lấy tất cả các mặt hàng (chi tiết sản phẩm) thuộc về 1 đơn hàng cụ thể
    List<OrderItem> findByOrderId(UUID orderId);
}
