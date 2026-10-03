package com.application.demo.service;

import com.application.demo.entity.Batch;
import com.application.demo.entity.Course;
import com.application.demo.repository.BatchRepository;
import com.application.demo.repository.CourseRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BatchService {
    private final BatchRepository batchRepository;
    private final CourseRepository courseRepository;

    public BatchService(BatchRepository batchRepository, CourseRepository courseRepository) {
        this.batchRepository = batchRepository;
        this.courseRepository = courseRepository;
    }

    public List<Batch> getAllBatches() { return batchRepository.findAll(); }

    public Batch getBatchById(Long id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found: " + id));
    }

    public List<Batch> getBatchesByCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found: " + courseId);
        }
        return batchRepository.findByCourseId(courseId);
    }

    public Batch createBatch(Long courseId, Batch batch) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + courseId));
        batch.setCourse(course);
        if (batch.getActive() == null) batch.setActive(true);
        return batchRepository.save(batch);
    }

    public Batch updateBatch(Long id, Long courseId, Batch updatedBatch) {
        Batch batch = getBatchById(id);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + courseId));
        batch.setBatchName(updatedBatch.getBatchName());
        batch.setStartDate(updatedBatch.getStartDate());
        batch.setTiming(updatedBatch.getTiming());
        batch.setMaxStudents(updatedBatch.getMaxStudents());
        if (updatedBatch.getActive() != null) batch.setActive(updatedBatch.getActive());
        batch.setCourse(course);
        return batchRepository.save(batch);
    }

    public void deleteBatch(Long id) {
        Batch batch = getBatchById(id);
        batchRepository.delete(batch);
    }
}
