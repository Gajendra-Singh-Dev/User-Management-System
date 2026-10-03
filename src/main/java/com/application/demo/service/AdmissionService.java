package com.application.demo.service;

import com.application.demo.dto.AdmissionRequest;
import com.application.demo.entity.Admission;
import com.application.demo.entity.Batch;
import com.application.demo.entity.Course;
import com.application.demo.entity.Enquiry;
import com.application.demo.entity.EnquiryStatus;
import com.application.demo.entity.Student;
import com.application.demo.exception.ResourceNotFoundException;
import com.application.demo.repository.AdmissionRepository;
import com.application.demo.repository.BatchRepository;
import com.application.demo.repository.EnquiryRepository;
import com.application.demo.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

@Service
@Transactional
public class AdmissionService {

    private final AdmissionRepository admissionRepository;
    private final EnquiryRepository enquiryRepository;
    private final BatchRepository batchRepository;
    private final StudentRepository studentRepository;

    public AdmissionService(AdmissionRepository admissionRepository,
                            EnquiryRepository enquiryRepository,
                            BatchRepository batchRepository,
                            StudentRepository studentRepository) {
        this.admissionRepository = admissionRepository;
        this.enquiryRepository = enquiryRepository;
        this.batchRepository = batchRepository;
        this.studentRepository = studentRepository;
    }

    public Admission admitEnquiry(Long enquiryId, AdmissionRequest request) {
        Enquiry enquiry = enquiryRepository.findById(enquiryId)
                .orElseThrow(() -> new ResourceNotFoundException("Enquiry not found: " + enquiryId));

        if (admissionRepository.existsByEnquiryId(enquiryId)
                || enquiry.getStatus() == EnquiryStatus.ADMITTED) {
            throw new IllegalStateException("This enquiry is already admitted: " + enquiryId);
        }

        Course course = enquiry.getInterestedCourse();
        if (course == null || course.getId() == null) {
            throw new IllegalArgumentException("Interested course is required for admission");
        }

        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found: " + request.getBatchId()));

        if (batch.getCourse() == null || !course.getId().equals(batch.getCourse().getId())) {
            throw new IllegalArgumentException("Selected batch does not belong to the enquiry course");
        }

        if (Boolean.FALSE.equals(batch.getActive())) {
            throw new IllegalArgumentException("Selected batch is inactive");
        }

        double courseFee = course.getFee() == null ? 0.0 : course.getFee();
        double discount = request.getDiscount() == null ? 0.0 : request.getDiscount();

        if (discount > courseFee) {
            throw new IllegalArgumentException("Discount cannot be greater than course fee");
        }

        Student student = new Student();
        student.setStudentName(enquiry.getStudentName());
        student.setMobile(enquiry.getMobile());
        student.setEmail(enquiry.getEmail());
        student.setQualification(enquiry.getQualification());
        student.setCourse(course);
        student.setBatch(batch);
        student.setCreatedAt(LocalDateTime.now());
        student = studentRepository.save(student);

        Admission admission = new Admission();
        admission.setAdmissionNumber(generateAdmissionNumber());
        admission.setEnquiry(enquiry);
        admission.setStudent(student);
        admission.setCourse(course);
        admission.setBatch(batch);
        admission.setCourseFee(courseFee);
        admission.setDiscount(discount);
        admission.setFinalFee(courseFee - discount);
        admission.setAdmissionDate(LocalDate.now());

        Admission savedAdmission = admissionRepository.save(admission);

        enquiry.setStatus(EnquiryStatus.ADMITTED);
        enquiryRepository.save(enquiry);

        return savedAdmission;
    }

    @Transactional(readOnly = true)
    public List<Admission> getAllAdmissions() {
        return admissionRepository.findAll();
    }

    private String generateAdmissionNumber() {
        String year = String.valueOf(Year.now().getValue());
        String prefix = "TF-" + year + "-";

        return admissionRepository.findTopByAdmissionNumberStartingWithOrderByIdDesc(prefix)
                .map(last -> {
                    String numberPart = last.getAdmissionNumber().substring(prefix.length());
                    int next = Integer.parseInt(numberPart) + 1;
                    return prefix + String.format("%04d", next);
                })
                .orElse(prefix + "0001");
    }
}
