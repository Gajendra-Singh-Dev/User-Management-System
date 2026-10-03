package com.application.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class AdmissionRequest {

    @NotNull(message = "Batch ID is required")
    private Long batchId;

    @NotNull(message = "Discount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Discount cannot be negative")
    private Double discount;

    public AdmissionRequest() {
    }

    public Long getBatchId() { return batchId; }
    public void setBatchId(Long batchId) { this.batchId = batchId; }

    public Double getDiscount() { return discount; }
    public void setDiscount(Double discount) { this.discount = discount; }
}
