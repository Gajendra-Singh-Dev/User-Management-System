package com.application.demo.service;

import com.application.demo.entity.Course;
import com.application.demo.entity.Enquiry;
import com.application.demo.entity.EnquiryStatus;
import com.application.demo.exception.ResourceNotFoundException;
import com.application.demo.repository.CourseRepository;
import com.application.demo.repository.EnquiryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class EnquiryServiceImpl implements EnquiryService {

    private final EnquiryRepository enquiryRepository;
    private final CourseRepository courseRepository;

    public EnquiryServiceImpl(EnquiryRepository enquiryRepository,
                              CourseRepository courseRepository) {
        this.enquiryRepository = enquiryRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Enquiry createEnquiry(Enquiry enquiry) {
        Course course = validateAndGetCourse(enquiry);
        enquiry.setInterestedCourse(course);

        if (enquiry.getStatus() == null) {
            enquiry.setStatus(EnquiryStatus.NEW);
        }
        if (enquiry.getCreatedAt() == null) {
            enquiry.setCreatedAt(LocalDateTime.now());
        }

        return enquiryRepository.save(enquiry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Enquiry> getAllEnquiries() {
        return enquiryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Enquiry getEnquiryById(Long id) {
        return enquiryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enquiry not found with id: " + id));
    }

    @Override
    public Enquiry updateEnquiry(Long id, Enquiry updatedEnquiry) {
        Enquiry existing = getEnquiryById(id);

        existing.setStudentName(updatedEnquiry.getStudentName());
        existing.setMobile(updatedEnquiry.getMobile());
        existing.setEmail(updatedEnquiry.getEmail());
        existing.setQualification(updatedEnquiry.getQualification());
        existing.setSource(updatedEnquiry.getSource());
        existing.setEnquiryDate(updatedEnquiry.getEnquiryDate());
        existing.setStatus(updatedEnquiry.getStatus());
        existing.setFollowUpDate(updatedEnquiry.getFollowUpDate());
        existing.setRemarks(updatedEnquiry.getRemarks());

        if (updatedEnquiry.getInterestedCourse() != null
                && updatedEnquiry.getInterestedCourse().getId() != null) {
            Course course = courseRepository.findById(updatedEnquiry.getInterestedCourse().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Course not found with id: " + updatedEnquiry.getInterestedCourse().getId()));
            existing.setInterestedCourse(course);
        }

        return enquiryRepository.save(existing);
    }

    @Override
    public void deleteEnquiry(Long id) {
        Enquiry existing = getEnquiryById(id);
        enquiryRepository.delete(existing);
    }

    private Course validateAndGetCourse(Enquiry enquiry) {
        if (enquiry.getInterestedCourse() == null
                || enquiry.getInterestedCourse().getId() == null) {
            throw new IllegalArgumentException("Course ID is required");
        }

        Long courseId = enquiry.getInterestedCourse().getId();

        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Course not found with id: " + courseId));
    }
}
