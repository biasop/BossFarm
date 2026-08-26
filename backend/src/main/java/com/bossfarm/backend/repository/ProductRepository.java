package com.bossfarm.backend.repository;

import com.bossfarm.backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    // Tìm sản phẩm theo slug (đường dẫn chi tiết sản phẩm chuẩn SEO)
    Optional<Product> findBySlug(String slug);

    // Kiểm tra slug đã tồn tại chưa (dùng khi thêm mới/sửa sản phẩm)
    boolean existsBySlug(String slug);

    // Lấy danh sách sản phẩm nổi bật đang kinh doanh (hiển thị trang chủ)
    List<Product> findByIsFeaturedTrueAndIsActiveTrue();

    // Lấy toàn bộ sản phẩm đang kinh doanh có phân trang
    Page<Product> findByIsActiveTrue(Pageable pageable);

    // Tìm kiếm sản phẩm theo tên (không phân biệt hoa/thường) có phân trang
    Page<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String keyword, Pageable pageable);
}
