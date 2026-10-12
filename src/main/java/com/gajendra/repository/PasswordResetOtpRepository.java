
package com.gajendra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gajendra.entity.PasswordResetOtp;

public interface PasswordResetOtpRepository
        extends JpaRepository<PasswordResetOtp, Long> {

    Optional<PasswordResetOtp> findTopByEmailOrderByIdDesc(
            String email
    );
}
