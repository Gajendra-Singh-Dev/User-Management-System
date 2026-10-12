package com.gajendra.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.gajendra.entity.Batch;
import com.gajendra.service.AuditLogService;
import com.gajendra.service.BatchService;

@RestController
@RequestMapping("/api/batches")
public class BatchController {

    private final BatchService batchService;
    private final AuditLogService auditLogService;

    public BatchController(
            BatchService batchService,
            AuditLogService auditLogService) {

        this.batchService = batchService;
        this.auditLogService = auditLogService;
    }

    // ==========================================
    // CREATE BATCH
    // ADMIN + STAFF
    // ==========================================

    @PostMapping
    public ResponseEntity<Batch> createBatch(
            @RequestBody Batch batch,
            Authentication authentication) {

        Batch savedBatch =
                batchService.createBatch(batch);

        String userEmail = authentication.getName();

        auditLogService.createLog(
                userEmail,
                "CREATE",
                "BATCH",
                "New batch created: " + savedBatch.getId()
        );

        return new ResponseEntity<>(
                savedBatch,
                HttpStatus.CREATED
        );
    }

    // ==========================================
    // GET ALL BATCHES
    // ==========================================

    @GetMapping
    public ResponseEntity<List<Batch>> getAllBatches() {

        return ResponseEntity.ok(
                batchService.getAllBatches()
        );
    }

    // ==========================================
    // GET BATCH BY ID
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<Batch> getBatchById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                batchService.getBatchById(id)
        );
    }

    // ==========================================
    // UPDATE BATCH
    // ADMIN + STAFF
    // ==========================================

    @PutMapping("/{id}")
    public ResponseEntity<Batch> updateBatch(
            @PathVariable Long id,
            @RequestBody Batch batch,
            Authentication authentication) {

        Batch updatedBatch =
                batchService.updateBatch(
                        id,
                        batch
                );

        String userEmail = authentication.getName();

        auditLogService.createLog(
                userEmail,
                "UPDATE",
                "BATCH",
                "Batch updated: " + updatedBatch.getId()
        );

        return ResponseEntity.ok(updatedBatch);
    }

    // ==========================================
    // DELETE BATCH
    // ADMIN + STAFF
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBatch(
            @PathVariable Long id,
            Authentication authentication) {

        // Delete se pehle existing batch nikal rahe hain
        Batch existingBatch =
                batchService.getBatchById(id);

        // Batch delete
        batchService.deleteBatch(id);

        String userEmail = authentication.getName();

        // Audit log create
        auditLogService.createLog(
                userEmail,
                "DELETE",
                "BATCH",
                "Batch deleted: " + existingBatch.getId()
        );

        return ResponseEntity.ok(
                "Batch deleted successfully"
        );
    }
}