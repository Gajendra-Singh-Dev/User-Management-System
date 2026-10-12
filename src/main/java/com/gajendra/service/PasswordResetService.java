
package com.gajendra.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gajendra.entity.PasswordResetOtp;
import com.gajendra.entity.User;
import com.gajendra.repository.PasswordResetOtpRepository;
import com.gajendra.repository.UserRepository;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetOtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private final SecureRandom secureRandom = new SecureRandom();

    public PasswordResetService(
            UserRepository userRepository,
            PasswordResetOtpRepository otpRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {

        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    // ==========================================
    // FORGOT PASSWORD - SEND OTP
    // ==========================================

    @Transactional
    public void forgotPassword(String email) {

        Optional<User> userOptional = userRepository.findByEmail(email);

        // Same response for registered and unregistered emails
        if (userOptional.isEmpty()) {
            return;
        }

        // Generate a 6-digit OTP
        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000)
        );

        PasswordResetOtp resetOtp = new PasswordResetOtp();

        resetOtp.setEmail(email);
        resetOtp.setOtpHash(passwordEncoder.encode(otp));
        resetOtp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        resetOtp.setVerified(false);
        resetOtp.setUsed(false);

        otpRepository.save(resetOtp);

        // Send OTP to registered email
        emailService.sendOtpEmail(email, otp);
    }

    // ==========================================
    // VERIFY OTP
    // ==========================================

    @Transactional
    public void verifyOtp(String email, String otp) {

        PasswordResetOtp resetOtp = getValidOtp(email, otp);

        resetOtp.setVerified(true);

        otpRepository.save(resetOtp);
    }

    // ==========================================
    // RESET PASSWORD
    // ==========================================

    @Transactional
    public void resetPassword(
            String email,
            String otp,
            String newPassword,
            String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException(
                    "New password and confirm password do not match"
            );
        }

        PasswordResetOtp resetOtp = getValidOtp(email, otp);

        if (!resetOtp.isVerified()) {
            throw new IllegalArgumentException(
                    "Please verify OTP before resetting password"
            );
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid password reset request")
                );

        // Store password using BCrypt
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // OTP can be used only once
        resetOtp.setUsed(true);
        otpRepository.save(resetOtp);
    }

    // ==========================================
    // COMMON OTP VALIDATION
    // ==========================================

    private PasswordResetOtp getValidOtp(String email, String otp) {

        PasswordResetOtp resetOtp = otpRepository
                .findTopByEmailOrderByIdDesc(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid or expired OTP")
                );

        if (resetOtp.isUsed()) {
            throw new IllegalArgumentException(
                    "OTP has already been used"
            );
        }

        if (resetOtp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "OTP has expired"
            );
        }

        if (!passwordEncoder.matches(otp, resetOtp.getOtpHash())) {
            throw new IllegalArgumentException(
                    "Invalid OTP"
            );
        }

        return resetOtp;
    }
}
