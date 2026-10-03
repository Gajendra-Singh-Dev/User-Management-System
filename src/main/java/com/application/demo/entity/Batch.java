package com.application.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "batches")
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "batchName is required")
    @Column(nullable = false)
    private String batchName;

    private LocalDate startDate;
    private String timing;

    @Min(value = 1, message = "maxStudents must be at least 1")
    private Integer maxStudents;

    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonBackReference
    private Course course;

    public Batch() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBatchName() { return batchName; }
    public void setBatchName(String batchName) { this.batchName = batchName; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public String getTiming() { return timing; }
    public void setTiming(String timing) { this.timing = timing; }
    public Integer getMaxStudents() { return maxStudents; }
    public void setMaxStudents(Integer maxStudents) { this.maxStudents = maxStudents; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
}
