package com.bossfarm.backend.dto;

import com.bossfarm.backend.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO phản hồi thông tin an toàn của người dùng (không chứa mật khẩu)")
public class UserResponse {

    @Schema(description = "Mã định danh duy nhất của người dùng (UUID)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Tên đăng nhập", example = "nguyen_van_a")
    private String username;

    @Schema(description = "Địa chỉ email", example = "nguyenvana@gmail.com")
    private String email;

    @Schema(description = "Họ và tên đầy đủ", example = "Nguyễn Văn A")
    private String fullName;

    @Schema(description = "Vai trò người dùng trong hệ thống", example = "CUSTOMER")
    private UserRole role;

    @Schema(description = "Trạng thái hoạt động của tài khoản", example = "true")
    private Boolean isActive;

    @Schema(description = "Thời gian tạo tài khoản", example = "2026-09-05T10:00:00Z")
    private OffsetDateTime createdAt;

    @Schema(description = "Thời gian đăng nhập gần nhất", example = "2026-09-05T10:30:00Z")
    private OffsetDateTime lastLoginAt;
}
