
package com.gajendra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gajendra.entity.LoginActivity;

public interface LoginActivityRepository
        extends JpaRepository<LoginActivity, Long> {

    List<LoginActivity> findByUserIdOrderByLoginDateTimeDesc(
            Long userId
    );
}

