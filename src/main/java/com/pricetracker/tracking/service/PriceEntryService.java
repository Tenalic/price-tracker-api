package com.pricetracker.tracking.service;

import com.pricetracker.auth.repository.UserRepository;
import com.pricetracker.exception.ForbiddenException;
import com.pricetracker.exception.ResourceNotFoundException;
import com.pricetracker.model.PriceEntry;
import com.pricetracker.model.Product;
import com.pricetracker.model.User;
import com.pricetracker.products.repository.ProductRepository;
import com.pricetracker.tracking.dto.PriceEntryRequest;
import com.pricetracker.tracking.dto.PriceEntryResponse;
import com.pricetracker.tracking.repository.PriceEntryRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PriceEntryService {
    private final PriceEntryRepository priceEntryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public PriceEntryService(PriceEntryRepository priceEntryRepository, ProductRepository productRepository,
                             UserRepository userRepository) {
        this.priceEntryRepository = priceEntryRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<PriceEntryResponse> getEntriesByProduct(String userEmail, UUID productId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Verify product exists and belongs to the user
        Product product = productRepository.findByIdAndUserId(productId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        List<PriceEntry> entries = priceEntryRepository.findByProductIdOrderByCreatedAtDesc(productId);

        return entries.stream()
                .map(e -> new PriceEntryResponse(e.getId(), e.getProductId(), e.getPrice(), e.getStore(), e.getDate(), e.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public PriceEntryResponse createEntry(String userEmail, UUID productId, PriceEntryRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Verify product exists and belongs to the user
        Product product = productRepository.findByIdAndUserId(productId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        PriceEntry newEntry = new PriceEntry(productId, request.getPrice(), request.getStore(), request.getDate());
        PriceEntry savedEntry = priceEntryRepository.save(newEntry);

        // Update product's updatedAt timestamp
        product.setUpdatedAt(Instant.now());
        productRepository.save(product);

        return new PriceEntryResponse(savedEntry.getId(), savedEntry.getProductId(), savedEntry.getPrice(),
                savedEntry.getStore(), savedEntry.getDate(), savedEntry.getCreatedAt());
    }

    public PriceEntryResponse updateEntry(String userEmail, UUID productId, UUID entryId, PriceEntryRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Verify product exists and belongs to the user
        Product product = productRepository.findByIdAndUserId(productId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Verify entry exists and belongs to the product
        PriceEntry entry = priceEntryRepository.findByIdAndProductId(entryId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Price entry not found"));

        entry.setPrice(request.getPrice());
        entry.setStore(request.getStore());
        entry.setDate(request.getDate());

        PriceEntry updatedEntry = priceEntryRepository.save(entry);

        // Update product's updatedAt timestamp
        product.setUpdatedAt(Instant.now());
        productRepository.save(product);

        return new PriceEntryResponse(updatedEntry.getId(), updatedEntry.getProductId(), updatedEntry.getPrice(),
                updatedEntry.getStore(), updatedEntry.getDate(), updatedEntry.getCreatedAt());
    }

    public void deleteEntry(String userEmail, UUID productId, UUID entryId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Verify product exists and belongs to the user
        Product product = productRepository.findByIdAndUserId(productId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Verify entry exists and belongs to the product
        PriceEntry entry = priceEntryRepository.findByIdAndProductId(entryId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Price entry not found"));

        priceEntryRepository.delete(entry);

        // Recalculate product's updatedAt
        Optional<PriceEntry> lastEntry = priceEntryRepository.findMostRecentByProductId(productId);
        if (lastEntry.isPresent()) {
            product.setUpdatedAt(lastEntry.get().getCreatedAt());
        } else {
            product.setUpdatedAt(Instant.now());
        }
        productRepository.save(product);
    }
}
