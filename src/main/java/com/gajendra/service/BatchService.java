package com.gajendra.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gajendra.entity.Batch;
import com.gajendra.repository.BatchRepository;

@Service
public class BatchService {

    private final BatchRepository batchRepository;

    public BatchService(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    public Batch createBatch(Batch batch) {

        if (batch.getStatus() == null ||
                batch.getStatus().isBlank()) {

            batch.setStatus("ACTIVE");
        }

        return batchRepository.save(batch);
    }

    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    public Batch getBatchById(Long id) {

        return batchRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Batch not found with id: " + id
                    )
                );
    }

    public Batch updateBatch(
            Long id,
            Batch batch) {

        Batch existing =
                getBatchById(id);

        existing.setBatchName(
                batch.getBatchName()
        );

        existing.setCourseName(
                batch.getCourseName()
        );

        existing.setTrainerName(
                batch.getTrainerName()
        );

        existing.setStartDate(
                batch.getStartDate()
        );

        existing.setEndDate(
                batch.getEndDate()
        );

        existing.setStatus(
                batch.getStatus()
        );

        return batchRepository.save(existing);
    }

    public void deleteBatch(Long id) {

        Batch existing =
                getBatchById(id);

        batchRepository.delete(existing);
    }
}