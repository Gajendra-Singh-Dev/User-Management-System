package com.gajendra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gajendra.entity.Enquiry;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {

    List<Enquiry> findByEmailOrderByIdDesc(String email);
}