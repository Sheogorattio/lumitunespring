/*package com.blacksabbath.lumitunespring.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "images")
public class Image {
	@Id
	UUID id;
	
	@Column(nullable = false)
	String originalName;
	
	@OneToOne
	@Column(nullable = false)
	UUID uploadedBy;
	
	@Column(nullable = false)
	String mimType;
	
	@Column(nullable = false)
	int originalSize;
	
}
*/