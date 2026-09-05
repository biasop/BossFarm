package com.bossfarm.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO chi tiết từng mặt hàng trong đơn hàng")
public class OrderItemResponse {

    @Schema(description = "ID bản ghi order item", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "ID sản phẩm", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID productId;

    @Schema(description = "Tên sản phẩm tại thời điểm mua", example = "Phân bón lá sinh học NPK 30-10-10")
    private String productName;

    @Schema(description = "Ảnh thu nhỏ sản phẩm", example = "https://bossfarm.com/images/npk.jpg")
    private String productThumbnailUrl;

    @Schema(description = "Số lượng đặt mua", example = "2")
    private Integer quantity;

    @Schema(description = "Đơn giá tại thời điểm mua (VNĐ)", example = "150000.00")
    private BigDecimal unitPrice;

    @Schema(description = "Tổng tiền cho món hàng này (unitPrice * quantity)", example = "300000.00")
    private BigDecimal totalPrice;
}
