package com.gajendra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gajendra.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
