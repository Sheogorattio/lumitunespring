package com.blacksabbath.lumitunespring.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blacksabbath.lumitunespring.model.Image;

public interface ImageRepository extends JpaRepository<Image, UUID> {
	public Optional<Image> findByUrl(String url);
}
