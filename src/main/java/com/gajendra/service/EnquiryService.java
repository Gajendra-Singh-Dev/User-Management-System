package com.gajendra.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gajendra.entity.Enquiry;
import com.gajendra.repository.EnquiryRepository;

@Service
public class EnquiryService {

    private final EnquiryRepository enquiryRepository;

    public EnquiryService(EnquiryRepository enquiryRepository) {
        this.enquiryRepository = enquiryRepository;
    }

    // CREATE
    public Enquiry createEnquiry(Enquiry enquiry) {
        return enquiryRepository.save(enquiry);
    }

    // READ ALL
    public List<Enquiry> getAllEnquiries() {
        return enquiryRepository.findAll();
    }

    // READ BY ID
    public Enquiry getEnquiryById(Long id) {

        return enquiryRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Enquiry not found with id: " + id
                    )
                );
    }

    // UPDATE
    public Enquiry updateEnquiry(Long id, Enquiry enquiry) {

        Enquiry existingEnquiry =
                enquiryRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Enquiry not found with id: " + id
                    )
                );

        existingEnquiry.setStudentName(
                enquiry.getStudentName()
        );

        existingEnquiry.setEmail(
                enquiry.getEmail()
        );

        existingEnquiry.setMobile(
                enquiry.getMobile()
        );

        existingEnquiry.setCourse(
                enquiry.getCourse()
        );

        existingEnquiry.setEnquiryDate(
                enquiry.getEnquiryDate()
        );

        existingEnquiry.setStatus(
                enquiry.getStatus()
        );

        existingEnquiry.setRemarks(
                enquiry.getRemarks()
        );

        return enquiryRepository.save(existingEnquiry);
    }

    // DELETE
    public void deleteEnquiry(Long id) {

        Enquiry existingEnquiry =
                enquiryRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Enquiry not found with id: " + id
                    )
                );

        enquiryRepository.delete(existingEnquiry);
    }
}