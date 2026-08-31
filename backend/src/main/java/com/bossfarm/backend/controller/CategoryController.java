package com.bossfarm.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bossfarm.backend.service.CategoryService;
import com.bossfarm.backend.dto.CategoryRequest;
import com.bossfarm.backend.dto.CategoryResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Category Management", description = "API quản lý danh mục phân bón")
@RequiredArgsConstructor // tạo constructor và phải truyền những thằng cần thiết, ví dụ như những thuộc
                         // tính final
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "Lấy toàn bộ cây danh mục", description = "Trả về danh sách tất cả các danh mục")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategoriesTree() {
        List<CategoryResponse> categories = categoryService.getAllCategoriesTree();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable UUID id) {
        CategoryResponse category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<CategoryResponse> getCategoryBySlug(@PathVariable String slug) {
        CategoryResponse category = categoryService.getCategoryBySlug(slug);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse createdCategory = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable UUID id,
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse updatedCategory = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
