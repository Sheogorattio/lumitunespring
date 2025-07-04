package com.blacksabbath.lumitunespring.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blacksabbath.lumitunespring.model.Playlist;
import com.blacksabbath.lumitunespring.model.PlaylistTrack;
import com.blacksabbath.lumitunespring.model.Track;

public interface PlaylistTrackRepository extends JpaRepository<PlaylistTrack, UUID> {
	public List<PlaylistTrack> findByPlaylist(Playlist playlist);
	public List<PlaylistTrack> findByTrack(Track track);
	
	public Optional<PlaylistTrack> findByPlaylistAndTrack(Playlist playlist, Track track); 
}
