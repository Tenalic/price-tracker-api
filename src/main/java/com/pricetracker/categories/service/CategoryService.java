package com.pricetracker.categories.service;

import com.pricetracker.auth.repository.UserRepository;
import com.pricetracker.categories.dto.CategoryRequest;
import com.pricetracker.categories.dto.CategoryResponse;
import com.pricetracker.categories.repository.CategoryRepository;
import com.pricetracker.exception.ConflictException;
import com.pricetracker.exception.ForbiddenException;
import com.pricetracker.exception.ResourceNotFoundException;
import com.pricetracker.model.Category;
import com.pricetracker.model.User;
import com.pricetracker.products.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<CategoryResponse> getAllCategories(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Category> categories = categoryRepository.findByUserIdIsNullOrUserId(user.getId());

        return categories.stream()
                .map(cat -> new CategoryResponse(cat.getId(), cat.getName(), cat.getIcon(), cat.getUserId() != null))
                .collect(Collectors.toList());
    }

    public CategoryResponse createCategory(String userEmail, CategoryRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Category newCategory = new Category(request.getName(), request.getIcon(), user.getId());
        Category savedCategory = categoryRepository.save(newCategory);

        return new CategoryResponse(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getIcon(),
                true  // Always custom when created by user
        );
    }

    public void deleteCategory(String userEmail, UUID categoryId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        // Check if global category (cannot delete)
        if (category.isGlobal()) {
            throw new ForbiddenException("Cannot delete a global category");
        }

        // Check if owned by current user
        if (!category.getUserId().equals(user.getId())) {
            throw new ForbiddenException("You cannot delete a category that belongs to another user");
        }

        // Check if any products use this category
        long productCount = productRepository.countByCategoryId(categoryId);
        if (productCount > 0) {
            throw new ConflictException("Cannot delete category that contains products");
        }

        categoryRepository.delete(category);
    }
}
