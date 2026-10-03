package com.application.demo.dto;

public class StudentAttendanceSummary {

    private Long studentId;
    private String studentName;
    private long totalClasses;
    private long present;
    private long absent;
    private double attendancePercentage;

    public StudentAttendanceSummary() {
    }

    public StudentAttendanceSummary(
            Long studentId,
            String studentName,
            long totalClasses,
            long present,
            long absent,
            double attendancePercentage) {

        this.studentId = studentId;
        this.studentName = studentName;
        this.totalClasses = totalClasses;
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

    public long getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(long totalClasses) {
        this.totalClasses = totalClasses;
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