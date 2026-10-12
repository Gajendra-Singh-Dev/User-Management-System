package com.gajendra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gajendra.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByBatchId(Long batchId);

    long countByBatchId(Long batchId);
}