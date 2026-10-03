package com.application.demo.repository;

import com.application.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByBatchId(Long batchId);

    long countByBatchId(Long batchId);
}