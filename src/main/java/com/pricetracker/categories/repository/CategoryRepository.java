package com.pricetracker.categories.repository;

import com.pricetracker.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByUserIdIsNullOrUserId(UUID userId);

    long countByUserIdAndId(UUID userId, UUID id);
}
