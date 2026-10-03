package com.application.demo.service;

import com.application.demo.entity.Batch;
import com.application.demo.entity.Course;
import com.application.demo.repository.CourseRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) { this.courseRepository = courseRepository; }

    public List<Course> getAllCourses() { return courseRepository.findAll(); }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + id));
    }

    public Course createCourse(Course course) {
        if (course.getActive() == null) course.setActive(true);
        if (course.getBatches() != null) {
            for (Batch batch : course.getBatches()) batch.setCourse(course);
        }
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course updatedCourse) {
        Course course = getCourseById(id);
        course.setCourseName(updatedCourse.getCourseName());
        course.setDescription(updatedCourse.getDescription());
        course.setDurationMonths(updatedCourse.getDurationMonths());
        course.setFee(updatedCourse.getFee());
        if (updatedCourse.getActive() != null) course.setActive(updatedCourse.getActive());
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        courseRepository.delete(course);
    }
}
