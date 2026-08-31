package com.bossfarm.backend.dto;

import java.util.List;
import java.util.UUID;

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
public class CategoryResponse {
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private UUID parentId;
    private String parentName;
    private List<CategoryResponse> subCategories;
}
