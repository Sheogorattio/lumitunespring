package com.blacksabbath.lumitunespring.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blacksabbath.lumitunespring.model.Playlist;
import com.blacksabbath.lumitunespring.model.User;

public interface PlaylistRepository extends JpaRepository<Playlist, UUID> {
	public List<Playlist> findByUser(User user);
	
	public List<Playlist> findByUserId(UUID id);
	
	public Optional<Playlist> findByName(String name);
}
