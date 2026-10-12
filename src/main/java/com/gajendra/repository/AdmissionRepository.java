

package com.gajendra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gajendra.entity.Admission;

public interface AdmissionRepository
        extends JpaRepository<Admission, Long> {
}