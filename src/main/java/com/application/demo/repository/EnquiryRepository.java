package com.application.demo.repository;

import com.application.demo.entity.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {
}
