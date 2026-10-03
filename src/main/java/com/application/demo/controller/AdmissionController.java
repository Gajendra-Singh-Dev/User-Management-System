package com.application.demo.controller;

import com.application.demo.dto.AdmissionRequest;
import com.application.demo.entity.Admission;
import com.application.demo.service.AdmissionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AdmissionController {

    private final AdmissionService admissionService;

    public AdmissionController(AdmissionService admissionService) {
        this.admissionService = admissionService;
    }

    @PostMapping("/enquiries/{enquiryId}/admit")
    public ResponseEntity<Admission> admitEnquiry(
            @PathVariable Long enquiryId,
            @Valid @RequestBody AdmissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(admissionService.admitEnquiry(enquiryId, request));
    }

    @GetMapping("/admissions")
    public ResponseEntity<List<Admission>> getAllAdmissions() {
        return ResponseEntity.ok(admissionService.getAllAdmissions());
    }
}
