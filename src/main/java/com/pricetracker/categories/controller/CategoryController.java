package com.pricetracker.categories.controller;

import com.pricetracker.categories.dto.CategoryRequest;
import com.pricetracker.categories.dto.CategoryResponse;
import com.pricetracker.categories.service.CategoryService;
import com.pricetracker.security.SecurityContextHelper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final SecurityContextHelper securityContextHelper;

    public CategoryController(CategoryService categoryService, SecurityContextHelper securityContextHelper) {
        this.categoryService = categoryService;
        this.securityContextHelper = securityContextHelper;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        String userEmail = securityContextHelper.getCurrentUserEmailOrThrow();
        List<CategoryResponse> categories = categoryService.getAllCategories(userEmail);
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        String userEmail = securityContextHelper.getCurrentUserEmailOrThrow();
        CategoryResponse response = categoryService.createCategory(userEmail, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        String userEmail = securityContextHelper.getCurrentUserEmailOrThrow();
        categoryService.deleteCategory(userEmail, id);
        return ResponseEntity.noContent().build();
    }
}
