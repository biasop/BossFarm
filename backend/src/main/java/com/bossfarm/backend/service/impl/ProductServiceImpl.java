package com.bossfarm.backend.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bossfarm.backend.repository.ProductRepository;
import com.bossfarm.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import com.bossfarm.backend.model.Product;
import com.bossfarm.backend.dto.ProductRequest;
import com.bossfarm.backend.dto.ProductResponse;
import com.bossfarm.backend.exception.ResourceNotFoundException;

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
    @Override
    @Transactional
    public ProductResponse updateProduct(UUID id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        if (!product.getSlug().equals(request.getSlug()) && productRepository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Slug '" + request.getSlug() + "' đã được sử dụng bởi sản phẩm khác!");
        }
        product.setName(request.getName());
        product.setSlug(request.getSlug());
        product.setThumbnailUrl(request.getThumbnailUrl());
        product.setPrice(request.getPrice());
        product.setComposition(request.getComposition());
        product.setBenefits(request.getBenefits());
        product.setUsageInstructions(request.getUsageInstructions());
        product.setStorageWarnings(request.getStorageWarnings());

        if (request.getIsFeatured() != null) {
            product.setIsFeatured(request.getIsFeatured());
        }
        if (request.getIsActive() != null) {
            product.setIsActive(request.getIsActive());
        }

        // Lưu lại (Hibernate sẽ tự kích hoạt @UpdateTimestamp để cập nhật thời gian
        // updatedAt)
        Product updatedProduct = productRepository.save(product);
        return mapToResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return productRepository.existsById(id);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return productRepository.existsBySlug(slug);
    }

    @Override
    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return mapToResponse(product);
    }

    @Override
    public ProductResponse getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "slug", slug));
        return mapToResponse(product);
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findByIsActiveTrue(pageable).map(this::mapToResponse);
    }

    /*
     * [1. Client gửi yêu cầu] ──► Ví dụ: "Lấy trang 0, mỗi trang 10 sản phẩm"
     * │
     * ▼
     * [2. productRepository.findByIsActiveTrue(pageable)]
     * │ • Database chạy SQL: SELECT ... LIMIT 10 OFFSET 0
     * │ • Database chạy đếm: SELECT COUNT(*) ...
     * ▼
     * [3. Trả về đối tượng Page<Product>]
     * │ • Bên trong chứa: 10 Entity Product
     * │ • Kèm Metadata: totalElements = 100, totalPages = 10, currentPage = 0
     * ▼
     * [4. .map(this::mapToResponse)]
     * │ • Lấy từng Product (1, 2, ..., 10) ──► Chuyển thành ProductResponse (1, 2,
     * ..., 10)
     * │ • Giữ nguyên thông tin Metadata phân trang
     * ▼
     * [5. Trả về đối tượng Page<ProductResponse>]
     * │
     * ▼
     * [6. Gửi về Frontend dạng JSON]
     * 
     */
    @Override
    public List<ProductResponse> getFeaturedProducts() {
        return productRepository.findByIsFeaturedTrueAndIsActiveTrue().stream().map(this::mapToResponse).toList();
    }

    /*
     * [1. Gọi Repository lấy dữ liệu từ DB]
     * │ • Database chạy SQL: SELECT * FROM products WHERE is_featured = true AND
     * is_active = true;
     * │ • Trả về: List<Product> (Tập hợp các Entity gốc)
     * ▼
     * [2. .stream()]
     * │ • Mở một dòng chảy dữ liệu (Băng chuyền xử lý)
     * │ • Đưa từng phần tử Product vào luồng xử lý tuần tự
     * ▼
     * [3. .map(this::mapToResponse)] <-- Intermediate Operation (Khâu trung gian)
     * │ • Từng Product đi qua khâu này: Product ──► mapToResponse() ──►
     * ProductResponse
     * │ • Đầu ra của khâu này là một luồng Stream<ProductResponse>
     * ▼
     * [4. .toList()] <-- Terminal Operation (Khâu đóng gói kết thúc)
     * │ • Kích hoạt toàn bộ luồng Stream chạy
     * │ • Thu gom tất cả các phần tử ProductResponse lại
     * │ • Đóng gói thành một danh sách mới: List<ProductResponse>
     * ▼
     * [5. Controller nhận List<ProductResponse> & Trả về JSON cho Frontend]
     * Ví dụ: [ { "name": "SP 1", "isFeatured": true }, { "name": "SP 2",
     * "isFeatured": true } ]
     */

    @Override
    public Page<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return getAllProducts(pageable);
        }
        return productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(keyword.trim(), pageable)
                .map(this::mapToResponse);
    }
}
