package com.bossfarm.backend.service;

import java.util.List;
import java.util.UUID;
import com.bossfarm.backend.dto.CategoryRequest;
import com.bossfarm.backend.dto.CategoryResponse;

public interface CategoryService {
    List<CategoryResponse> getAllCategoriesTree();

    CategoryResponse getCategoryById(UUID id);

    CategoryResponse getCategoryBySlug(String slug);

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory(UUID id, CategoryRequest request);

    void deleteCategory(UUID id);

    boolean existsBySlug(String slug);

    boolean existsById(UUID id);
}
