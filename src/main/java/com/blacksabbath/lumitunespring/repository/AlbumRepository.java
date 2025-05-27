package com.blacksabbath.lumitunespring.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blacksabbath.lumitunespring.model.Album;
import com.blacksabbath.lumitunespring.model.Artist;

public interface AlbumRepository extends JpaRepository<Album, UUID> {
	public List<Album> findByArtist(Artist artist);
}
