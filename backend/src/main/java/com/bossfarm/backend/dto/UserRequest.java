package com.bossfarm.backend.dto;

import com.bossfarm.backend.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO dùng để tạo mới hoặc đăng ký người dùng")
public class UserRequest {

    @NotBlank(message = "Username không được để trống")
    @Size(min = 3, max = 50, message = "Username phải từ 3 đến 50 ký tự")
    @Schema(description = "Tên đăng nhập duy nhất", example = "nguyen_van_a")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Size(max = 255, message = "Email không vượt quá 255 ký tự")
    @Schema(description = "Địa chỉ email duy nhất", example = "nguyenvana@gmail.com")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 100, message = "Mật khẩu phải có từ 6 đến 100 ký tự")
    @Schema(description = "Mật khẩu người dùng", example = "Password@123")
    private String password;

    @Size(max = 255, message = "Họ và tên không vượt quá 255 ký tự")
    @Schema(description = "Họ và tên đầy đủ", example = "Nguyễn Văn A")
    private String fullName;

    @Schema(description = "Vai trò người dùng (CUSTOMER, FARMER, ADMIN)", example = "CUSTOMER")
    private UserRole role;

    @Schema(description = "Trạng thái kích hoạt tài khoản", example = "true")
    private Boolean isActive;
}
