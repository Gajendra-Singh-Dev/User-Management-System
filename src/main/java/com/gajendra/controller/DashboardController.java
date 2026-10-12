package com.gajendra.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gajendra.repository.AdmissionRepository;
import com.gajendra.repository.AttendanceRepository;
import com.gajendra.repository.BatchRepository;
import com.gajendra.repository.EnquiryRepository;
import com.gajendra.repository.UserRepository;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final UserRepository userRepository;
    private final EnquiryRepository enquiryRepository;
    private final AdmissionRepository admissionRepository;
    private final BatchRepository batchRepository;
    private final AttendanceRepository attendanceRepository;

    public DashboardController(
            UserRepository userRepository,
            EnquiryRepository enquiryRepository,
            AdmissionRepository admissionRepository,
            BatchRepository batchRepository,
            AttendanceRepository attendanceRepository) {

        this.userRepository = userRepository;
        this.enquiryRepository = enquiryRepository;
        this.admissionRepository = admissionRepository;
        this.batchRepository = batchRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @GetMapping("/statistics")
    public ResponseEntity<DashboardStatistics> getStatistics() {

        DashboardStatistics statistics = new DashboardStatistics();

        statistics.setTotalUsers(userRepository.count());
        statistics.setTotalEnquiries(enquiryRepository.count());
        statistics.setTotalAdmissions(admissionRepository.count());
        statistics.setTotalBatches(batchRepository.count());
        statistics.setTotalAttendance(attendanceRepository.count());

        return ResponseEntity.ok(statistics);
    }
}