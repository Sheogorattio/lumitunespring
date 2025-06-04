package com.blacksabbath.lumitunespring.service;

import org.springframework.stereotype.Service;

import com.blacksabbath.lumitunespring.repository.EmailVerificationRepository;

@Service
public class EmailVerificationService {

	private final EmailVerificationRepository emailVerificationRepository;
	
	public EmailVerificationService(EmailVerificationRepository emailVerificationRepository) {
		this.emailVerificationRepository = emailVerificationRepository;
	}
	
	public void deleteUnverifiedUsers() {
		
	}
}
                    