package com.blacksabbath.lumitunespring.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blacksabbath.lumitunespring.model.Artist;
import com.blacksabbath.lumitunespring.model.User;

public interface ArtistRepository extends JpaRepository<Artist, UUID> {
	public Optional<Artist> findByUser(User user);
	public Optional<Artist> findByUserUsername(String username);
}
