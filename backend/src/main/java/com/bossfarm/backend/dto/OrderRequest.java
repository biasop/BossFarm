package com.bossfarm.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO dùng để gửi yêu cầu đặt hàng (Checkout)")
public class OrderRequest {

    @Schema(description = "ID người dùng đặt hàng (tùy chọn nếu mua không cần đăng nhập)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID userId;

    @NotBlank(message = "Tên người nhận không được để trống")
    @Size(max = 255, message = "Tên người nhận không vượt quá 255 ký tự")
    @Schema(description = "Họ và tên người nhận hàng", example = "Trần Thị B")
    private String recipientName;

    @NotBlank(message = "Số điện thoại nhận hàng không được để trống")
    @Size(max = 20, message = "Số điện thoại không vượt quá 20 ký tự")
    @Schema(description = "Số điện thoại liên hệ", example = "0987654321")
    private String phoneNumber;

    @NotBlank(message = "Địa chỉ giao hàng không được để trống")
    @Schema(description = "Địa chỉ giao hàng chi tiết (thôn, xã, huyện, tỉnh)", example = "Số 123 đường Giải Phóng, Phường Đồng Tâm, Hai Bà Trưng, Hà Nội")
    private String shippingAddress;

    @Schema(description = "Ghi chú đơn hàng từ khách hàng", example = "Giao hàng vào giờ hành chính, gọi trước khi giao")
    private String notes;

    @NotEmpty(message = "Đơn hàng phải có ít nhất 1 sản phẩm")
    @Valid
    @Schema(description = "Danh sách các mặt hàng đặt mua")
    private List<OrderItemRequest> items;
}
