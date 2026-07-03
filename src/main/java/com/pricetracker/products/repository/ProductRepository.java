package com.pricetracker.products.repository;

import com.pricetracker.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByUserId(UUID userId);

    @Query("SELECT p FROM Product p WHERE p.userId = :userId AND LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Product> findByUserIdAndNameContainingIgnoreCase(@Param("userId") UUID userId, @Param("search") String search);

    List<Product> findByUserIdAndCategoryId(UUID userId, UUID categoryId);

    @Query("SELECT p FROM Product p WHERE p.userId = :userId AND p.categoryId = :categoryId AND LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Product> findByUserIdAndCategoryIdAndNameContaining(@Param("userId") UUID userId, @Param("categoryId") UUID categoryId, @Param("search") String search);

    Optional<Product> findByIdAndUserId(UUID id, UUID userId);

    boolean existsByIdAndUserId(UUID id, UUID userId);

    long countByCategoryId(UUID categoryId);
}
