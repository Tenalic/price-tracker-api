package com.pricetracker.tracking.controller;

import com.pricetracker.security.SecurityContextHelper;
import com.pricetracker.tracking.dto.PriceEntryRequest;
import com.pricetracker.tracking.dto.PriceEntryResponse;
import com.pricetracker.tracking.service.PriceEntryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products/{productId}/entries")
public class PriceEntryController {
    private final PriceEntryService priceEntryService;
    private final SecurityContextHelper securityContextHelper;

    public PriceEntryController(PriceEntryService priceEntryService, SecurityContextHelper securityContextHelper) {
        this.priceEntryService = priceEntryService;
        this.securityContextHelper = securityContextHelper;
    }

    @GetMapping
    public ResponseEntity<List<PriceEntryResponse>> getEntries(@PathVariable UUID productId) {
        String userEmail = securityContextHelper.getCurrentUserEmailOrThrow();
        List<PriceEntryResponse> entries = priceEntryService.getEntriesByProduct(userEmail, productId);
        return ResponseEntity.ok(entries);
    }

    @PostMapping
    public ResponseEntity<PriceEntryResponse> createEntry(
            @PathVariable UUID productId,
            @Valid @RequestBody PriceEntryRequest request) {
        String userEmail = securityContextHelper.getCurrentUserEmailOrThrow();
        PriceEntryResponse response = priceEntryService.createEntry(userEmail, productId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{entryId}")
    public ResponseEntity<PriceEntryResponse> updateEntry(
            @PathVariable UUID productId,
            @PathVariable UUID entryId,
            @Valid @RequestBody PriceEntryRequest request) {
        String userEmail = securityContextHelper.getCurrentUserEmailOrThrow();
        PriceEntryResponse response = priceEntryService.updateEntry(userEmail, productId, entryId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{entryId}")
    public ResponseEntity<Void> deleteEntry(
            @PathVariable UUID productId,
            @PathVariable UUID entryId) {
        String userEmail = securityContextHelper.getCurrentUserEmailOrThrow();
        priceEntryService.deleteEntry(userEmail, productId, entryId);
        return ResponseEntity.noContent().build();
    }
}
