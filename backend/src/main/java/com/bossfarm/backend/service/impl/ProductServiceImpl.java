package com.bossfarm.backend.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bossfarm.backend.repository.ProductRepository;
import com.bossfarm.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import com.bossfarm.backend.model.Product;
import com.bossfarm.backend.dto.ProductRequest;
import com.bossfarm.backend.dto.ProductResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .thumbnailUrl(product.getThumbnailUrl())
                .price(product.getPrice())
                .composition(product.getComposition())
                .benefits(product.getBenefits())
                .usageInstructions(product.getUsageInstructions())
                .storageWarnings(product.getStorageWarnings())
                .createdById(product.getCreatedBy() != null ? product.getCreatedBy().getId() : null)
                .createdByName(product.getCreatedBy() != null ? product.getCreatedBy().getUsername() : null)
                .isFeatured(product.getIsFeatured())
                .isActive(product.getIsActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Slug '" + request.getSlug() + "' đã tồn tại!");
        }
        Product product = Product.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .thumbnailUrl(request.getThumbnailUrl())
                .price(request.getPrice())
                .composition(request.getComposition())
                .benefits(request.getBenefits())
                .usageInstructions(request.getUsageInstructions())
                .storageWarnings(request.getStorageWarnings())
                .isFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
    }

    /*
     * BẮT ĐẦU HÀM createProduct()
     * │
     * ├─► [1. @Transactional]: Mở một Phiên làm việc (Session) & Bắt đầu
     * Transaction (conn.setAutoCommit(false))
     * │
     * ├─► [2. Code của bạn]: Build đối tượng Product (trong RAM)
     * │
     * ├─► [3. productRepository.save(product)]:
     * │ • Đưa object vào bộ quản lý (Persistence Context)
     * │ • Kích hoạt các Annotation (@GeneratedValue sinh UUID, @CreationTimestamp
     * lấy giờ)
     * │ • Chuẩn bị câu lệnh SQL INSERT
     * │
     * ├─► [4. mapToResponse(savedProduct)]: Map dữ liệu trả về cho Controller
     * │
     * ▼
     * KẾT THÚC HÀM createProduct()
     * │
     * └─► [5. @Transactional]:
     * • Thực hiện "Flush" (Bắn lệnh SQL INSERT xuống PostgreSQL)
     * • Thực hiện "Commit" (Lưu vĩnh viễn xuống đĩa cứng)
     * • Đóng phiên kết nối (Trả Connection về Pool)
     * • (Nếu có lỗi ở bất kỳ dòng nào -> Tự động Rollback lại như chưa có gì xảy
     * ra)
     * 
     */
    public ProductResponse updateProduct(UUID id, ProductRequest request) {
        return null;
    }

}
