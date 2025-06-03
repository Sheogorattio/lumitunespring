package com.blacksabbath.lumitunespring.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blacksabbath.lumitunespring.dto.PlaylistDto;
import com.blacksabbath.lumitunespring.mapper.PlaylistMapper;
import com.blacksabbath.lumitunespring.model.Playlist;
import com.blacksabbath.lumitunespring.model.PlaylistTrack;
import com.blacksabbath.lumitunespring.model.Track;
import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.repository.PlaylistRepository;
import com.blacksabbath.lumitunespring.repository.PlaylistTrackRepository;

@Service
public class PlaylistService {
	
	private final PlaylistRepository playlistRepository;
	
	private final PlaylistMapper playlistMapper;
	
	private final PlaylistTrackRepository playlistTrackRepository;
	
	public PlaylistService(PlaylistRepository playlistRepository, PlaylistMapper playlistMapper, PlaylistTrackRepository playlistTrackRepository) {
		this.playlistRepository= playlistRepository;
		this.playlistMapper= playlistMapper;
		this.playlistTrackRepository= playlistTrackRepository;
	}
	
	@Transactional(readOnly= true)
	public List<PlaylistDto> findPlaylistsByUser(User user){
		return playlistRepository.findByUser(user)
				.stream()
				.map(pl -> playlistMapper.toDto(pl, false))
				.toList();
	}
	
	@Transactional(readOnly= true)
	public Optional<PlaylistDto> findPlaylistById(UUID id){
		return Optional.ofNullable(playlistRepository.findById(id).map(pl -> playlistMapper.toDto(pl, false)).orElse(null));
	}
	
	@Transactional(readOnly= true)
	public Optional<PlaylistDto> findPlaylistByName(String name){
		return Optional.ofNullable(playlistRepository.findByName(name).map(pl -> playlistMapper.toDto(pl, false)).orElse(null));
	}
	
	@Transactional
	public void deletePlaylist(Playlist entity) {
		playlistRepository.delete(entity);
	}
	
	@Transactional
	public PlaylistDto addTrack(Track track, Playlist playlist) throws Exception {
		List<PlaylistTrack> pl_tr = playlist.getTracks();
		boolean alreadyExists = pl_tr.stream().anyMatch(pt -> pt.getTrack().equals(track));
		if(alreadyExists) {
			throw new Exception("Track is already in playlist");
		}
		int newOrderNumber = pl_tr.size()+1;
		pl_tr.add(new PlaylistTrack(null, playlist, track, newOrderNumber));
		playlistRepository.save(playlist);
		return playlistMapper.toDto(playlist, false);
	}
	
	@Transactional
	public PlaylistDto removeTrack(Track track, Playlist playlist) {
		List<PlaylistTrack> pl_tr = playlist.getTracks();
		Iterator<PlaylistTrack> iterator = pl_tr.iterator();
		int newOrder = 0;
		while(iterator.hasNext()) {
			PlaylistTrack e = iterator.next();
			if(e.getTrack().equals(track)) {
				iterator.remove();
			}
			else {
				e.setTrackOrder(++newOrder);
			}
		}
		playlistRepository.save(playlist);
		return playlistMapper.toDto(playlist, false);
	}
}
