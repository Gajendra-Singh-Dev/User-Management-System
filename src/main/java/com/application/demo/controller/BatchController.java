package com.application.demo.controller;

import com.application.demo.entity.Batch;
import com.application.demo.entity.Student;
import com.application.demo.service.BatchService;
import com.application.demo.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batches")
public class BatchController {

    private final BatchService batchService;
    private final StudentService studentService;

    public BatchController(BatchService batchService,
                           StudentService studentService) {
        this.batchService = batchService;
        this.studentService = studentService;
    }

    @GetMapping
    public List<Batch> getAllBatches() {
        return batchService.getAllBatches();
    }

    @GetMapping("/{id}")
    public Batch getBatchById(@PathVariable Long id) {
        return batchService.getBatchById(id);
    }

    @GetMapping("/course/{courseId}")
    public List<Batch> getBatchesByCourse(
            @PathVariable Long courseId) {

        return batchService.getBatchesByCourse(courseId);
    }

    @PostMapping("/course/{courseId}")
    public ResponseEntity<Batch> createBatch(
            @PathVariable Long courseId,
            @Valid @RequestBody Batch batch) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(batchService.createBatch(courseId, batch));
    }

    @PutMapping("/{id}/course/{courseId}")
    public Batch updateBatch(
            @PathVariable Long id,
            @PathVariable Long courseId,
            @Valid @RequestBody Batch batch) {

        return batchService.updateBatch(id, courseId, batch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBatch(
            @PathVariable Long id) {

        batchService.deleteBatch(id);
        return ResponseEntity.noContent().build();
    }

    // Task 4: Get all students of a particular batch
    @GetMapping("/{batchId}/students")
    public ResponseEntity<List<Student>> getBatchStudents(
            @PathVariable Long batchId) {

        return ResponseEntity.ok(
                studentService.getStudentsByBatch(batchId)
        );
    }
}