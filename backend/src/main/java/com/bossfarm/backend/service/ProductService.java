package com.bossfarm.backend.service;

import java.util.UUID;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.bossfarm.backend.dto.ProductRequest;
import com.bossfarm.backend.dto.ProductResponse;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(UUID id, ProductRequest request);

    void deleteProduct(UUID id);

    boolean existsBySlug(String slug);

    boolean existsById(UUID id);

    ProductResponse getProductById(UUID id);

    ProductResponse getProductBySlug(String slug);

    Page<ProductResponse> getAllProducts(Pageable pageable);

    Page<ProductResponse> searchProducts(String keyword, Pageable pageable);

    List<ProductResponse> getFeaturedProducts();
}
