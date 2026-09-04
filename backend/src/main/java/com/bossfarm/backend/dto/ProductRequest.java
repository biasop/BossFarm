package com.bossfarm.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO dùng để tạo mới hoặc cập nhật sản phẩm phân bón")
public class ProductRequest {

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 255, message = "Tên sản phẩm không vượt quá 255 ký tự")
    @Schema(description = "Tên sản phẩm phân bón", example = "Phân bón lá sinh học NPK 30-10-10")
    private String name;

    @Size(max = 255, message = "Slug không vượt quá 255 ký tự")
    @Schema(description = "Slug chuẩn SEO (nếu để trống backend sẽ tự tạo từ tên)", example = "phan-bon-la-sinh-hoc-npk-30-10-10")
    private String slug;

    @Size(max = 500, message = "URL ảnh thu nhỏ không vượt quá 500 ký tự")
    @Schema(description = "URL hình ảnh đại diện của sản phẩm", example = "https://bossfarm.com/images/npk-30-10-10.jpg")
    private String thumbnailUrl;

    @NotNull(message = "Giá sản phẩm không được để trống")
    @Positive(message = "Giá sản phẩm phải lớn hơn 0")
    @Schema(description = "Giá bán sản phẩm (VNĐ)", example = "150000.00")
    private BigDecimal price;

    @Size(max = 5000, message = "Thành phần phân bón không vượt quá 5000 ký tự")
    @Schema(description = "Thành phần phân bón", example = "Đạm tổng số (Nts): 30%, Lân hữu hiệu (P2O5hh): 10%, Kali hữu hiệu (K2Ohh): 10%, TE (vi lượng)")
    private String composition;

    @Size(max = 5000, message = "Công dụng sản phẩm không vượt quá 5000 ký tự")
    @Schema(description = "Công dụng / Lợi ích cho cây trồng", example = "Kích thích đọt non phát triển cực mạnh, đẻ nhánh khỏe, lá xanh dày bóng mượt")
    private String benefits;

    @Size(max = 5000, message = "Hướng dẫn sử dụng không vượt quá 5000 ký tự")
    @Schema(description = "Hướng dẫn liều lượng và cách sử dụng", example = "Pha 20 - 25g cho bình 16 - 25 lít nước. Phun định kỳ 7 - 10 ngày/lần")
    private String usageInstructions;

    @Size(max = 5000, message = "Cảnh báo bảo quản không vượt quá 5000 ký tự")
    @Schema(description = "Hướng dẫn bảo quản & Cảnh báo an toàn", example = "Bảo quản nơi khô ráo thoáng mát, tránh ánh nắng trực tiếp và xa tầm tay trẻ em")
    private String storageWarnings;

    @Schema(description = "Đánh dấu sản phẩm nổi bật hiển thị ở trang chủ", example = "false")
    private Boolean isFeatured;

    @Schema(description = "Trạng thái hiển thị sản phẩm (true: đang bán, false: tạm ẩn)", example = "true")
    private Boolean isActive;
}
