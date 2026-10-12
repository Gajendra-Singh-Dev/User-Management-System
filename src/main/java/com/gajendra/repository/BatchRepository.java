package com.gajendra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gajendra.entity.Batch;

public interface BatchRepository extends JpaRepository<Batch, Long> {

}