package com.application.demo.repository;

import com.application.demo.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BatchRepository extends JpaRepository<Batch, Long> {
    List<Batch> findByCourseId(Long courseId);
}
