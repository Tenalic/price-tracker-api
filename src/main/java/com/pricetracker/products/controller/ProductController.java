package com.pricetracker.products.controller;

import com.pricetracker.products.dto.ProductListResponse;
import com.pricetracker.products.dto.ProductRequest;
import com.pricetracker.products.dto.ProductResponse;
import com.pricetracker.products.service.ProductService;
import com.pricetracker.security.SecurityContextHelper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final SecurityContextHelper securityContextHelper;

    public ProductController(ProductService productService, SecurityContextHelper securityContextHelper) {
        this.productService = productService;
        this.securityContextHelper = securityContextHelper;
    }

    @GetMapping
    public ResponseEntity<List<ProductListResponse>> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID category) {
        String userEmail = securityContextHelper.getCurrentUserEmailOrThrow();
        List<ProductListResponse> products = productService.getProducts(userEmail, search, category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable UUID id) {
        String userEmail = securityContextHelper.getCurrentUserEmailOrThrow();
        ProductResponse product = productService.getProduct(userEmail, id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        String userEmail = securityContextHelper.getCurrentUserEmailOrThrow();
        ProductResponse response = productService.createProduct(userEmail, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequest request) {
        String userEmail = securityContextHelper.getCurrentUserEmailOrThrow();
        ProductResponse response = productService.updateProduct(userEmail, id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        String userEmail = securityContextHelper.getCurrentUserEmailOrThrow();
        productService.deleteProduct(userEmail, id);
        return ResponseEntity.noContent().build();
    }
}
