package com.gajendra.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.gajendra.entity.Enquiry;
import com.gajendra.service.AuditLogService;
import com.gajendra.service.EnquiryService;

@RestController
@RequestMapping("/api/enquiries")
public class EnquiryController {

    private final EnquiryService enquiryService;
    private final AuditLogService auditLogService;

    public EnquiryController(
            EnquiryService enquiryService,
            AuditLogService auditLogService) {

        this.enquiryService = enquiryService;
        this.auditLogService = auditLogService;
    }

    // ==========================================
    // CREATE ENQUIRY
    // ADMIN + STAFF
    // ==========================================

    @PostMapping
    public ResponseEntity<Enquiry> createEnquiry(
            @RequestBody Enquiry enquiry,
            Authentication authentication) {

        Enquiry savedEnquiry =
                enquiryService.createEnquiry(enquiry);

        String userEmail = authentication.getName();

        auditLogService.createLog(
                userEmail,
                "CREATE",
                "ENQUIRY",
                "New enquiry created: " + savedEnquiry.getId()
        );

        return new ResponseEntity<>(
                savedEnquiry,
                HttpStatus.CREATED
        );
    }

    // ==========================================
    // GET ALL ENQUIRIES
    // ==========================================

    @GetMapping
    public ResponseEntity<List<Enquiry>> getAllEnquiries() {

        return ResponseEntity.ok(
                enquiryService.getAllEnquiries()
        );
    }

    // ==========================================
    // MY ENQUIRIES
    // STUDENT
    // ==========================================

    @GetMapping("/my")
    public ResponseEntity<List<Enquiry>> getMyEnquiries(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                enquiryService.getMyEnquiries(email)
        );
    }

    // ==========================================
    // GET ENQUIRY BY ID
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<Enquiry> getEnquiryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                enquiryService.getEnquiryById(id)
        );
    }

    // ==========================================
    // UPDATE ENQUIRY
    // ADMIN + STAFF
    // ==========================================

    @PutMapping("/{id}")
    public ResponseEntity<Enquiry> updateEnquiry(
            @PathVariable Long id,
            @RequestBody Enquiry enquiry,
            Authentication authentication) {

        Enquiry updatedEnquiry =
                enquiryService.updateEnquiry(id, enquiry);

        String userEmail = authentication.getName();

        auditLogService.createLog(
                userEmail,
                "UPDATE",
                "ENQUIRY",
                "Enquiry updated: " + updatedEnquiry.getId()
        );

        return ResponseEntity.ok(updatedEnquiry);
    }

    // ==========================================
    // DELETE ENQUIRY
    // ADMIN + STAFF
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEnquiry(
            @PathVariable Long id,
            Authentication authentication) {

        Enquiry existingEnquiry =
                enquiryService.getEnquiryById(id);

        enquiryService.deleteEnquiry(id);

        String userEmail = authentication.getName();

        auditLogService.createLog(
                userEmail,
                "DELETE",
                "ENQUIRY",
                "Enquiry deleted: " + existingEnquiry.getId()
        );

        return ResponseEntity.ok(
                "Enquiry deleted successfully"
        );
    }
}