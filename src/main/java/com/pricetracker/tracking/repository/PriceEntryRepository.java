package com.pricetracker.tracking.repository;

import com.pricetracker.model.PriceEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PriceEntryRepository extends JpaRepository<PriceEntry, UUID> {
    List<PriceEntry> findByProductIdOrderByCreatedAtDesc(UUID productId);

    @Query("SELECT pe FROM PriceEntry pe WHERE pe.productId = :productId ORDER BY pe.date DESC, pe.createdAt DESC LIMIT 1")
    Optional<PriceEntry> findLatestByProductId(@Param("productId") UUID productId);

    Optional<PriceEntry> findByIdAndProductId(UUID id, UUID productId);

    boolean existsByIdAndProductId(UUID id, UUID productId);

    @Query("SELECT pe FROM PriceEntry pe WHERE pe.productId = :productId ORDER BY pe.date DESC, pe.createdAt DESC LIMIT 1")
    Optional<PriceEntry> findMostRecentByProductId(@Param("productId") UUID productId);
}
