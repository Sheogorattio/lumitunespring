package com.blacksabbath.lumitunespring.model;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class EmailVerification {

		@Id
		@GeneratedValue(strategy = GenerationType.UUID)
		private UUID id;
		
		@OneToOne
		@JoinColumn(name = "user_id", nullable = false)
		private User user;
		
		@Column(nullable = false)
		private Date deleteAfter;
		
		public EmailVerification() {
			
		}
		
		public EmailVerification(UUID id, User user, Date deleteAfter) {
			this.id = id;
			this.user = user;
			this.deleteAfter = deleteAfter;
		}
		
		public UUID getId() {
			return this.id;
		}
		
		public void setId(UUID id) {
			this.id = id;
		}
		
		public User getUser() {
			return this.user;
		}
		
		public void setuser(User user) {
			this.user = user;
		}
		
		public Date getDeleteAfter() {
			return this.deleteAfter;
		}
		
		public void setDeleteAfter(Date deleteAfter) {
			this.deleteAfter = deleteAfter;
		}
}
