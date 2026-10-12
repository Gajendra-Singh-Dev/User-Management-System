
package com.gajendra.dto;

public class BatchAttendanceSummary {

    private Long batchId;
    private String batchName;
    private Long totalClasses;
    private Long presentCount;
    private Long absentCount;
    private double attendancePercentage;

    // Default Constructor
    public BatchAttendanceSummary() {
    }

    // Parameterized Constructor
    public BatchAttendanceSummary(
            Long batchId,
            String batchName,
            Long totalClasses,
            Long presentCount,
            Long absentCount,
            double attendancePercentage) {

        this.batchId = batchId;
        this.batchName = batchName;
        this.totalClasses = totalClasses;
        this.presentCount = presentCount;
        this.absentCount = absentCount;
        this.attendancePercentage = attendancePercentage;
    }

    // Getters and Setters

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public Long getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(Long totalClasses) {
        this.totalClasses = totalClasses;
    }

    public Long getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(Long presentCount) {
        this.presentCount = presentCount;
    }

    public Long getAbsentCount() {
        return absentCount;
    }

    public void setAbsentCount(Long absentCount) {
        this.absentCount = absentCount;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }
}
