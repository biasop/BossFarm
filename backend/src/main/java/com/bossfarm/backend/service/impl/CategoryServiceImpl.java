package com.bossfarm.backend.service.impl;

import com.bossfarm.backend.dto.CategoryRequest;
import com.bossfarm.backend.dto.CategoryResponse;
import com.bossfarm.backend.exception.ResourceNotFoundException;
import com.bossfarm.backend.model.Category;
import com.bossfarm.backend.repository.CategoryRepository;
import com.bossfarm.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private CategoryResponse mapToResponse(Category category) {
        if (category == null) {
            return null;
        }

        List<CategoryResponse> subResponses = null;
        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
            subResponses = category.getSubCategories().stream().map(this::mapToResponse).toList();

        }

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .parentName(category.getParent() != null ? category.getParent().getName() : null)
                .subCategories(subResponses)
                .build();
    }

    @Override
    public List<CategoryResponse> getAllCategoriesTree() {
        return categoryRepository.findByParentIsNull().stream().map(this::mapToResponse).toList();
    }

    @Override
    public CategoryResponse getCategoryById(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return mapToResponse(category);
    }

    @Override
    public CategoryResponse getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "slug", slug));
        return mapToResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Slug " + request.getSlug() + " đã tồn tại");
        }

        Category parent = null;
        if (request.getParentId() != null) {
            parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "parentId", request.getParentId()));
        }

        Category category = Category.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .description(request.getDescription())
                .parent(parent)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return mapToResponse(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(UUID id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        if (!request.getSlug().equals(category.getSlug()) && categoryRepository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Slug " + request.getSlug() + " đã tồn tại");
        }

        Category parent = null;
        if (request.getParentId() != null) {
            parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "parentId", request.getParentId()));
        }

        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setDescription(request.getDescription());
        category.setParent(parent);

        Category savedCategory = categoryRepository.save(category);
        return mapToResponse(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category", "id", id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return categoryRepository.existsBySlug(slug);
    }

    @Override
    public boolean existsById(UUID id) {
        return categoryRepository.existsById(id);
    }
}
