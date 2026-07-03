package com.pricetracker.products.service;

import com.pricetracker.auth.repository.UserRepository;
import com.pricetracker.categories.dto.CategoryResponse;
import com.pricetracker.categories.repository.CategoryRepository;
import com.pricetracker.exception.ForbiddenException;
import com.pricetracker.exception.ResourceNotFoundException;
import com.pricetracker.model.Category;
import com.pricetracker.model.Product;
import com.pricetracker.model.PriceEntry;
import com.pricetracker.model.User;
import com.pricetracker.products.dto.ProductListResponse;
import com.pricetracker.products.dto.ProductRequest;
import com.pricetracker.products.dto.ProductResponse;
import com.pricetracker.products.repository.ProductRepository;
import com.pricetracker.tracking.repository.PriceEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PriceEntryRepository priceEntryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository,
                         UserRepository userRepository, PriceEntryRepository priceEntryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.priceEntryRepository = priceEntryRepository;
    }

    public List<ProductListResponse> getProducts(String userEmail, String search, UUID categoryId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Product> products;

        if (search != null && categoryId != null) {
            products = productRepository.findByUserIdAndCategoryIdAndNameContaining(user.getId(), categoryId, search);
        } else if (search != null) {
            products = productRepository.findByUserIdAndNameContainingIgnoreCase(user.getId(), search);
        } else if (categoryId != null) {
            products = productRepository.findByUserIdAndCategoryId(user.getId(), categoryId);
        } else {
            products = productRepository.findByUserId(user.getId());
        }

        return products.stream()
                .map(this::convertToListResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProduct(String userEmail, UUID productId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findByIdAndUserId(productId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Category category = categoryRepository.findById(product.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        CategoryResponse categoryResponse = new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getIcon(),
                category.getUserId() != null
        );

        return new ProductResponse(product.getId(), product.getName(), categoryResponse,
                product.getUnit(), product.getCreatedAt(), product.getUpdatedAt());
    }

    public ProductResponse createProduct(String userEmail, ProductRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Verify category exists and is accessible (global or owned by user)
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (category.getUserId() != null && !category.getUserId().equals(user.getId())) {
            throw new ForbiddenException("This category is not available to you");
        }

        Product newProduct = new Product(user.getId(), request.getCategoryId(), request.getName(), request.getUnit());
        Product savedProduct = productRepository.save(newProduct);

        CategoryResponse categoryResponse = new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getIcon(),
                category.getUserId() != null
        );

        return new ProductResponse(savedProduct.getId(), savedProduct.getName(), categoryResponse,
                savedProduct.getUnit(), savedProduct.getCreatedAt(), savedProduct.getUpdatedAt());
    }

    public ProductResponse updateProduct(String userEmail, UUID productId, ProductRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findByIdAndUserId(productId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Verify new category exists and is accessible
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (category.getUserId() != null && !category.getUserId().equals(user.getId())) {
            throw new ForbiddenException("This category is not available to you");
        }

        product.setName(request.getName());
        product.setCategoryId(request.getCategoryId());
        product.setUnit(request.getUnit());

        Product updatedProduct = productRepository.save(product);

        CategoryResponse categoryResponse = new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getIcon(),
                category.getUserId() != null
        );

        return new ProductResponse(updatedProduct.getId(), updatedProduct.getName(), categoryResponse,
                updatedProduct.getUnit(), updatedProduct.getCreatedAt(), updatedProduct.getUpdatedAt());
    }

    public void deleteProduct(String userEmail, UUID productId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findByIdAndUserId(productId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        productRepository.delete(product);
        // Cascade delete is handled by the database (ON DELETE CASCADE)
    }

    private ProductListResponse convertToListResponse(Product product) {
        // Fetch category details
        Category category = categoryRepository.findById(product.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        CategoryResponse categoryResponse = new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getIcon(),
                category.getUserId() != null
        );

        // Fetch latest price entry if exists
        Optional<PriceEntry> latestEntry = priceEntryRepository.findLatestByProductId(product.getId());

        return new ProductListResponse(
                product.getId(),
                product.getName(),
                categoryResponse,
                product.getUnit(),
                latestEntry.map(PriceEntry::getPrice).orElse(null),
                latestEntry.map(PriceEntry::getStore).orElse(null),
                product.getUpdatedAt()
        );
    }
}
