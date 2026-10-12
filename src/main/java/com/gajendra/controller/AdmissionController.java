package com.gajendra.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.gajendra.entity.Admission;
import com.gajendra.service.AdmissionService;
import com.gajendra.service.AuditLogService;

@RestController
@RequestMapping("/api/admissions")
public class AdmissionController {

    private final AdmissionService admissionService;
    private final AuditLogService auditLogService;

    public AdmissionController(
            AdmissionService admissionService,
            AuditLogService auditLogService) {

        this.admissionService = admissionService;
        this.auditLogService = auditLogService;
    }

    // ==========================================
    // CREATE ADMISSION
    // ADMIN + STAFF
    // ==========================================

    @PostMapping
    public ResponseEntity<Admission> createAdmission(
            @RequestBody Admission admission,
            Authentication authentication) {

        Admission savedAdmission =
                admissionService.createAdmission(admission);

        String userEmail = authentication.getName();

        auditLogService.createLog(
                userEmail,
                "CREATE",
                "ADMISSION",
                "New admission created: " + savedAdmission.getId()
        );

        return new ResponseEntity<>(
                savedAdmission,
                HttpStatus.CREATED
        );
    }

    // ==========================================
    // GET ALL ADMISSIONS
    // ==========================================

    @GetMapping
    public ResponseEntity<List<Admission>> getAllAdmissions() {

        return ResponseEntity.ok(
                admissionService.getAllAdmissions()
        );
    }

    // ==========================================
    // GET ADMISSION BY ID
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<Admission> getAdmissionById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                admissionService.getAdmissionById(id)
        );
    }

    // ==========================================
    // UPDATE ADMISSION
    // ADMIN + STAFF
    // ==========================================

    @PutMapping("/{id}")
    public ResponseEntity<Admission> updateAdmission(
            @PathVariable Long id,
            @RequestBody Admission admission,
            Authentication authentication) {

        Admission updatedAdmission =
                admissionService.updateAdmission(
                        id,
                        admission
                );

        String userEmail = authentication.getName();

        auditLogService.createLog(
                userEmail,
                "UPDATE",
                "ADMISSION",
                "Admission updated: " + updatedAdmission.getId()
        );

        return ResponseEntity.ok(updatedAdmission);
    }

    // ==========================================
    // DELETE ADMISSION
    // ADMIN + STAFF
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdmission(
            @PathVariable Long id,
            Authentication authentication) {

        Admission existingAdmission =
                admissionService.getAdmissionById(id);

        admissionService.deleteAdmission(id);

        String userEmail = authentication.getName();

        auditLogService.createLog(
                userEmail,
                "DELETE",
                "ADMISSION",
                "Admission deleted: " + existingAdmission.getId()
        );

        return ResponseEntity.ok(
                "Admission deleted successfully"
        );
    }
}