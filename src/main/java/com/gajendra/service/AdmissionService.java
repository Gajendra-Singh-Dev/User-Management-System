package com.gajendra.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gajendra.entity.Admission;
import com.gajendra.repository.AdmissionRepository;

@Service
public class AdmissionService {

    private final AdmissionRepository admissionRepository;

    public AdmissionService(AdmissionRepository admissionRepository) {
        this.admissionRepository = admissionRepository;
    }

    public Admission createAdmission(Admission admission) {

        admission.setAdmissionDate(LocalDateTime.now());

        if (admission.getStatus() == null ||
                admission.getStatus().isBlank()) {

            admission.setStatus("ACTIVE");
        }

        return admissionRepository.save(admission);
    }

    public List<Admission> getAllAdmissions() {
        return admissionRepository.findAll();
    }

    public Admission getAdmissionById(Long id) {

        return admissionRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Admission not found with id: " + id
                    )
                );
    }

    public Admission updateAdmission(
            Long id,
            Admission admission) {

        Admission existing =
                getAdmissionById(id);

        existing.setStudentName(
                admission.getStudentName()
        );

        existing.setStudentEmail(
                admission.getStudentEmail()
        );

        existing.setCourseName(
                admission.getCourseName()
        );

        existing.setStatus(
                admission.getStatus()
        );

        return admissionRepository.save(existing);
    }

    public void deleteAdmission(Long id) {

        Admission existing =
                getAdmissionById(id);

        admissionRepository.delete(existing);
    }
}