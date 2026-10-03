package com.application.demo.repository;

import com.application.demo.entity.Admission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdmissionRepository extends JpaRepository<Admission, Long> {

    boolean existsByEnquiryId(Long enquiryId);

    Optional<Admission> findTopByAdmissionNumberStartingWithOrderByIdDesc(String prefix);
}
