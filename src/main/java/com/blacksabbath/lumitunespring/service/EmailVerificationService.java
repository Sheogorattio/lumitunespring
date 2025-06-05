package com.blacksabbath.lumitunespring.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blacksabbath.lumitunespring.model.EmailVerification;
import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.repository.EmailVerificationRepository;
import com.blacksabbath.lumitunespring.repository.UserRepository;

@Service
public class EmailVerificationService {

	private final EmailVerificationRepository emailVerificationRepository;
	
	private final UserService userService;
	
	public EmailVerificationService(EmailVerificationRepository emailVerificationRepository, UserService userService) {
		this.emailVerificationRepository = emailVerificationRepository;
		this.userService = userService;
	}
	
	@Transactional
	@Scheduled(fixedRate = 30*60*1000 )
	public void deleteUnverifiedUsers() throws Exception {
		List<EmailVerification> terminatedAccounts = emailVerificationRepository.findAllByDeleteAfterBefore(new Date());
		Iterator<EmailVerification> iterator = terminatedAccounts.iterator();
		while(iterator.hasNext()) {
			EmailVerification _record = iterator.next();
			emailVerificationRepository.delete(_record);
			User user = _record.getUser();
			userService.delete(user);	 	
		}
	}
	
	@SuppressWarnings("deprecation")
	@Transactional 
	public EmailVerification createNew(User user) {
		Date terminalDate = new Date();
		terminalDate.setHours(terminalDate.getHours()+1);
		return emailVerificationRepository.save(new EmailVerification(null,user, terminalDate));
	}
	
	@Transactional
	public void verifyAccount(UUID recordId) throws NotFoundException {
		EmailVerification verificationRecord = emailVerificationRepository.findById(recordId).orElseThrow(() -> new NotFoundException());
		emailVerificationRepository.delete(verificationRecord);
	}
}
                    