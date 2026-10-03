package com.application.demo.controller;

import com.application.demo.entity.Course;
import com.application.demo.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;
    public CourseController(CourseService courseService) { this.courseService = courseService; }

    @GetMapping
    public List<Course> getAllCourses() { return courseService.getAllCourses(); }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable Long id) { return courseService.getCourseById(id); }

    @PostMapping
    public ResponseEntity<Course> createCourse(@Valid @RequestBody Course course) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(course));
    }

    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable Long id, @Valid @RequestBody Course course) {
        return courseService.updateCourse(id, course);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
