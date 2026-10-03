package com.application.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Student name is required")
    @Column(nullable = false)
    private String studentName;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Enter a valid 10 digit mobile number")
    @Column(nullable = false, length = 10)
    private String mobile;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Qualification is required")
    @Column(nullable = false)
    private String qualification;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "batches"})
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "batch_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "course"})
    private Batch batch;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Student() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Batch getBatch() { return batch; }
    public void setBatch(Batch batch) { this.batch = batch; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
