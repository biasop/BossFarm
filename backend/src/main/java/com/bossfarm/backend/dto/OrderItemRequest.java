package com.bossfarm.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO thông tin một món hàng trong giỏ/đơn hàng")
public class OrderItemRequest {

    @NotNull(message = "ID sản phẩm không được để trống")
    @Schema(description = "Mã sản phẩm (UUID)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID productId;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng sản phẩm mua phải lớn hơn hoặc bằng 1")
    @Schema(description = "Số lượng mua", example = "2")
    private Integer quantity;
}
