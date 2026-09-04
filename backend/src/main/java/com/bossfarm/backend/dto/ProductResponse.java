package com.bossfarm.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO phản hồi thông tin chi tiết của sản phẩm")
public class ProductResponse {

    @Schema(description = "Mã định danh duy nhất của sản phẩm (UUID)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Tên sản phẩm", example = "Phân bón lá sinh học NPK 30-10-10")
    private String name;

    @Schema(description = "Slug đường dẫn SEO", example = "phan-bon-la-sinh-hoc-npk-30-10-10")
    private String slug;

    @Schema(description = "Đường link ảnh đại diện sản phẩm", example = "https://bossfarm.com/images/npk-30-10-10.jpg")
    private String thumbnailUrl;

    @Schema(description = "Giá bán sản phẩm (VNĐ)", example = "150000.00")
    private BigDecimal price;

    @Schema(description = "Thành phần phân bón", example = "Đạm tổng số (Nts): 30%, Lân hữu hiệu (P2O5hh): 10%, Kali hữu hiệu (K2Ohh): 10%")
    private String composition;

    @Schema(description = "Công dụng / Lợi ích", example = "Kích thích đọt non, bung chồi mạnh, lá xanh dày bóng")
    private String benefits;

    @Schema(description = "Hướng dẫn sử dụng", example = "Pha 20 - 25g cho bình 16 - 25 lít nước")
    private String usageInstructions;

    @Schema(description = "Cảnh báo bảo quản & an toàn", example = "Bảo quản nơi khô ráo thoáng mát, xa tầm tay trẻ em")
    private String storageWarnings;

    @Schema(description = "ID của người tạo sản phẩm", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID createdById; // lưu ý

    @Schema(description = "Tên người tạo sản phẩm", example = "admin_bossfarm")
    private String createdByName; // lưu ý

    @Schema(description = "Sản phẩm nổi bật hiển thị ở trang chủ", example = "false")
    private Boolean isFeatured;

    @Schema(description = "Trạng thái hiển thị sản phẩm", example = "true")
    private Boolean isActive;

    @Schema(description = "Thời gian tạo sản phẩm", example = "2026-09-04T10:15:30Z")
    private OffsetDateTime createdAt;

    @Schema(description = "Thời gian cập nhật gần nhất", example = "2026-09-04T10:20:00Z")
    private OffsetDateTime updatedAt;
}
