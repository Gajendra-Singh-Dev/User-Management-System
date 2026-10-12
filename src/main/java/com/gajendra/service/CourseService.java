
package com.gajendra.service;

import com.gajendra.entity.Batch;
import com.gajendra.entity.Course;
import com.gajendra.exception.ResourceNotFoundException;
import com.gajendra.repository.CourseRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // Get all courses
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Get course by ID
    public Course getCourseById(Long id) {

        return courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Course not found: " + id
                        ));
    }

    // Create course
    public Course createCourse(Course course) {

        // Default active = true
        if (course.getActive() == null) {
            course.setActive(true);
        }

        // Set Course inside every Batch
        if (course.getBatches() != null) {

            for (Batch batch : course.getBatches()) {

                batch.setCourse(course);
            }
        }

        return courseRepository.save(course);
    }

    // Update course
    public Course updateCourse(Long id, Course updatedCourse) {

        Course course = getCourseById(id);

        course.setCourseName(updatedCourse.getCourseName());
        course.setDescription(updatedCourse.getDescription());
        course.setDurationMonths(updatedCourse.getDurationMonths());
        course.setFee(updatedCourse.getFee());

        if (updatedCourse.getActive() != null) {
            course.setActive(updatedCourse.getActive());
        }

        return courseRepository.save(course);
    }

    // Delete course
    public void deleteCourse(Long id) {

        Course course = getCourseById(id);

        courseRepository.delete(course);
    }
}

