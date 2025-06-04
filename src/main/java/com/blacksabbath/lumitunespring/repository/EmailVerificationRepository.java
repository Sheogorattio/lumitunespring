package com.blacksabbath.lumitunespring.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blacksabbath.lumitunespring.model.EmailVerification;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {
	public List<EmailVerification> findAllByDeleteAfterBefore(Date date);
}
