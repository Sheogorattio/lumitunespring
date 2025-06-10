package com.blacksabbath.lumitunespring.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blacksabbath.lumitunespring.misc.Genre;
import com.blacksabbath.lumitunespring.misc.Moods;
import com.blacksabbath.lumitunespring.model.Album;
import com.blacksabbath.lumitunespring.model.Track;

public interface TrackRepository extends JpaRepository<Track, UUID> {
	public List<Track> findByAlbum(Album album);
	public List<Track> findByName(String name);
	public List<Track> findByMoodsContaining(Moods mood);
	public List<Track> findByGenresContaining(Genre genre);
}
