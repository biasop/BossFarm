package com.bossfarm.backend.service;

import com.bossfarm.backend.dto.UserRequest;
import com.bossfarm.backend.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    // Tạo mới tài khoản người dùng
    UserResponse createUser(UserRequest request);

    // Lấy thông tin người dùng theo ID
    UserResponse getUserById(UUID id);

    // Lấy thông tin người dùng theo Username
    UserResponse getUserByUsername(String username);

    // Lấy thông tin người dùng theo Email
    UserResponse getUserByEmail(String email);

    // Cập nhật thông tin người dùng
    UserResponse updateUser(UUID id, UserRequest request);

    // Bật / Tắt trạng thái hoạt động của tài khoản (Khóa hoặc Mở khóa)
    void toggleUserStatus(UUID id);

    // Lấy danh sách toàn bộ người dùng có phân trang (dành cho Admin)
    Page<UserResponse> getAllUsers(Pageable pageable);

    // Kiểm tra tồn tại
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
