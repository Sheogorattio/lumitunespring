package com.blacksabbath.lumitunespring.service;

import java.util.UUID;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blacksabbath.lumitunespring.model.PlaylistTrack;
import com.blacksabbath.lumitunespring.repository.PlaylistTrackRepository;

@Service
public class PlaylistTrackService {
	
	private final PlaylistTrackRepository playlistTrackRepository;
	
	public PlaylistTrackService(PlaylistTrackRepository playlistTrackRepository) {
		this.playlistTrackRepository = playlistTrackRepository;
	}
	
	@Transactional(readOnly = true)
	public PlaylistTrack findPlaylistTrackEntityById(UUID id) throws NotFoundException {
		return playlistTrackRepository.findById(id).orElseThrow(() -> new NotFoundException());
	}
}
