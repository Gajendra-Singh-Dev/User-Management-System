package com.application.demo.dto;

public class BatchAttendanceSummary {

    private Long studentId;
    private String studentName;
    private long present;
    private long absent;
    private double attendancePercentage;

    public BatchAttendanceSummary() {
    }

    public BatchAttendanceSummary(
            Long studentId,
            String studentName,
            long present,
            long absent,
            double attendancePercentage) {

        this.studentId = studentId;
        this.studentName = studentName;
        this.present = present;
        this.absent = absent;
        this.attendancePercentage = attendancePercentage;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public long getPresent() {
        return present;
    }

    public void setPresent(long present) {
        this.present = present;
    }

    public long getAbsent() {
        return absent;
    }

    public void setAbsent(long absent) {
        this.absent = absent;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }
}