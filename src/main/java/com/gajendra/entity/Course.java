
package com.gajendra.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "courseName is required")
    @Column(nullable = false)
    private String courseName;

    @Column(length = 1000)
    private String description;

    @Min(value = 1, message = "durationMonths must be at least 1")
    private Integer durationMonths;

    @DecimalMin(
        value = "0.0",
        inclusive = true,
        message = "fee cannot be negative"
    )
    private Double fee;

    private Boolean active = true;

    // One Course has many Batches
    @OneToMany(
        mappedBy = "course",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonManagedReference
    @Valid
    private List<Batch> batches = new ArrayList<>();

    // Default constructor
    public Course() {
    }

    // Add Batch
    public void addBatch(Batch batch) {

        if (batch != null) {
            batches.add(batch);
            batch.setCourse(this);
        }
    }

    // Remove Batch
    public void removeBatch(Batch batch) {

        if (batch != null) {
            batches.remove(batch);
            batch.setCourse(null);
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Batch> getBatches() {
        return batches;
    }

    public void setBatches(List<Batch> batches) {

        this.batches.clear();

        if (batches != null) {
            for (Batch batch : batches) {
                addBatch(batch);
            }
        }
    }
}

