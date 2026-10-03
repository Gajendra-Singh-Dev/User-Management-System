package com.application.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "attendance",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_attendance_student_date",
                        columnNames = {"student_id", "attendance_date"}
                )
        }
)
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnoreProperties({
            "hibernateLazyInitializer",
            "handler",
            "batch",
            "course"
    })
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    @JsonIgnoreProperties({
            "hibernateLazyInitializer",
            "handler",
            "students",
            "course"
    })
    private Batch batch;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    public Attendance() {
    }

    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }
}