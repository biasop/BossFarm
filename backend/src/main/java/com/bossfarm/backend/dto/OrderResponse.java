package com.bossfarm.backend.dto;

import com.bossfarm.backend.enums.OrderStatus;
import com.bossfarm.backend.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO phản hồi chi tiết toàn bộ đơn hàng kèm danh sách sản phẩm")
public class OrderResponse {

    @Schema(description = "Mã định danh duy nhất của đơn hàng (UUID)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "ID của khách hàng đặt mua (nếu có)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID userId;

    @Schema(description = "Tên tài khoản người đặt (nếu có)", example = "nguyen_van_a")
    private String username;

    @Schema(description = "Tên người nhận hàng", example = "Trần Thị B")
    private String recipientName;

    @Schema(description = "Số điện thoại nhận hàng", example = "0987654321")
    private String phoneNumber;

    @Schema(description = "Địa chỉ nhận hàng", example = "Số 123 đường Giải Phóng, Hà Nội")
    private String shippingAddress;

    @Schema(description = "Ghi chú đơn hàng", example = "Giao giờ hành chính")
    private String notes;

    @Schema(description = "Tổng giá trị đơn hàng (VNĐ)", example = "450000.00")
    private BigDecimal totalAmount;

    @Schema(description = "Trạng thái thanh toán (UNPAID, PAID, REFUNDED)", example = "UNPAID")
    private PaymentStatus paymentStatus;

    @Schema(description = "Trạng thái xử lý đơn hàng (PENDING, PROCESSING, SHIPPING, COMPLETED, CANCELLED)", example = "PENDING")
    private OrderStatus status;

    @Schema(description = "Danh sách chi tiết các mặt hàng trong đơn")
    private List<OrderItemResponse> items;

    @Schema(description = "Thời gian đặt hàng", example = "2026-09-05T11:00:00Z")
    private OffsetDateTime createdAt;

    @Schema(description = "Thời gian cập nhật gần nhất", example = "2026-09-05T11:00:00Z")
    private OffsetDateTime updatedAt;
}
