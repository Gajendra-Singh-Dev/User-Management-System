package com.application.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "admissions", uniqueConstraints = {
        @UniqueConstraint(name = "uk_admission_number", columnNames = "admission_number"),
        @UniqueConstraint(name = "uk_admission_enquiry", columnNames = "enquiry_id")
})
public class Admission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admission_number", nullable = false, unique = true, length = 20)
    private String admissionNumber;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enquiry_id", nullable = false, unique = true)
    @JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler",
        "interestedCourse"
    })
    private Enquiry enquiry;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler",
        "course",
        "batch"
    })
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler",
        "batches"
    })
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "batch_id", nullable = false)
    @JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler",
        "course"
    })
    private Batch batch;
    
    @NotNull
    @DecimalMin(value = "0.0", inclusive = true, message = "Course fee cannot be negative")
    @Column(nullable = false)
    private Double courseFee;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true, message = "Discount cannot be negative")
    @Column(nullable = false)
    private Double discount;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true, message = "Final fee cannot be negative")
    @Column(nullable = false)
    private Double finalFee;

    @Column(nullable = false)
    private LocalDate admissionDate;

    public Admission() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAdmissionNumber() { return admissionNumber; }
    public void setAdmissionNumber(String admissionNumber) { this.admissionNumber = admissionNumber; }

    public Enquiry getEnquiry() { return enquiry; }
    public void setEnquiry(Enquiry enquiry) { this.enquiry = enquiry; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Batch getBatch() { return batch; }
    public void setBatch(Batch batch) { this.batch = batch; }

    public Double getCourseFee() { return courseFee; }
    public void setCourseFee(Double courseFee) { this.courseFee = courseFee; }

    public Double getDiscount() { return discount; }
    public void setDiscount(Double discount) { this.discount = discount; }

    public Double getFinalFee() { return finalFee; }
    public void setFinalFee(Double finalFee) { this.finalFee = finalFee; }

    public LocalDate getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(LocalDate admissionDate) { this.admissionDate = admissionDate; }
}
