package com.gajendra.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gajendra.entity.Enquiry;
import com.gajendra.service.EnquiryService;

@RestController
@RequestMapping("/api/enquiries")
public class EnquiryController {

    private final EnquiryService enquiryService;

    public EnquiryController(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    // CREATE ENQUIRY
    @PostMapping
    public ResponseEntity<Enquiry> createEnquiry(
            @RequestBody Enquiry enquiry) {

        Enquiry savedEnquiry =
                enquiryService.createEnquiry(enquiry);

        return new ResponseEntity<>(
                savedEnquiry,
                HttpStatus.CREATED
        );
    }

    // GET ALL ENQUIRIES
    @GetMapping
    public ResponseEntity<List<Enquiry>> getAllEnquiries() {

        return ResponseEntity.ok(
                enquiryService.getAllEnquiries()
        );
    }

    // GET ENQUIRY BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Enquiry> getEnquiryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                enquiryService.getEnquiryById(id)
        );
    }

    // UPDATE ENQUIRY
    @PutMapping("/{id}")
    public ResponseEntity<Enquiry> updateEnquiry(
            @PathVariable Long id,
            @RequestBody Enquiry enquiry) {

        return ResponseEntity.ok(
                enquiryService.updateEnquiry(id, enquiry)
        );
    }

    // DELETE ENQUIRY
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEnquiry(
            @PathVariable Long id) {

        enquiryService.deleteEnquiry(id);

        return ResponseEntity.ok(
                "Enquiry deleted successfully"
        );
    }
}