package com.gajendra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gajendra.entity.Enquiry;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {

}