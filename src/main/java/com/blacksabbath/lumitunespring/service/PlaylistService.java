package com.blacksabbath.lumitunespring.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blacksabbath.lumitunespring.dto.PlaylistDto;
import com.blacksabbath.lumitunespring.dto.UserDto;
import com.blacksabbath.lumitunespring.mapper.PlaylistMapper;
import com.blacksabbath.lumitunespring.model.Image;
import com.blacksabbath.lumitunespring.model.Playlist;
import com.blacksabbath.lumitunespring.model.PlaylistTrack;
import com.blacksabbath.lumitunespring.model.Track;
import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.repository.ImageRepository;
import com.blacksabbath.lumitunespring.repository.PlaylistRepository;
import com.blacksabbath.lumitunespring.repository.PlaylistTrackRepository;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class PlaylistService {
	
	private final PlaylistRepository playlistRepository;
	
	private final PlaylistMapper playlistMapper;
	
	private final PlaylistTrackRepository playlistTrackRepository;
	
	private final ImageRepository imageRepository;
	
	public PlaylistService(PlaylistRepository playlistRepository, PlaylistMapper playlistMapper, PlaylistTrackRepository playlistTrackRepository, ImageRepository imageRepository) {
		this.playlistRepository= playlistRepository;
		this.playlistMapper= playlistMapper;
		this.playlistTrackRepository= playlistTrackRepository;
		this.imageRepository = imageRepository;
	}
	
	@Transactional(readOnly= true)
	public List<PlaylistDto> findPlaylistsByUser(User user){
		return playlistRepository.findByUser(user)
				.stream()
				.map(pl -> playlistMapper.toDto(pl, false))
				.toList();
	}
	
	@Transactional(readOnly = true)
	public List<PlaylistDto> findPlaylistsByUserId(UUID id){
		return playlistRepository.findByUserId(id)
				.stream()
				.map(pl -> playlistMapper.toDto(pl, true))
				.toList();
				
	}
	
	
	
	@Transactional(readOnly= true)
	public PlaylistDto findPlaylistById(UUID id) throws Exception{
		Playlist playlist = playlistRepository.findById(id).orElseThrow(()-> new NotFoundException());
		return playlistMapper.toDto(playlist, true);
	}
	
	@Transactional(readOnly= true)
	public Playlist findEntityPlaylistById(UUID id) throws Exception{
		Playlist playlist = playlistRepository.findById(id).orElseThrow(()-> new NotFoundException());
		return playlist;
	}
	
	@Transactional(readOnly= true)
	public Optional<PlaylistDto> findPlaylistByName(String name){
		return Optional.ofNullable(playlistRepository.findByName(name).map(pl -> playlistMapper.toDto(pl, true)).orElse(null));
	}
	
	@Transactional
	public void deletePlaylist(Playlist entity) {
		playlistRepository.delete(entity);
	}
	
	@Transactional
	public PlaylistDto addTrack(Track track, Playlist playlist) throws Exception {
		List<PlaylistTrack> pl_tr = new ArrayList<>(playlist.getTracks());
		boolean alreadyExists = pl_tr.stream().anyMatch(pt -> pt.getTrack().equals(track));
		if(alreadyExists) {
			throw new Exception("Track is already in playlist");
		}
		int newOrderNumber = pl_tr.size()+1;
		pl_tr.add(playlistTrackRepository.save(new PlaylistTrack(null, playlist, track, newOrderNumber)));
		playlistRepository.save(playlist);
		return playlistMapper.toDto(playlist, true);
	}
	
	@Transactional
	public PlaylistDto removeTrack(Track track, Playlist playlist) {
		List<PlaylistTrack> pl_tr = new ArrayList<>(playlist.getTracks());
		Iterator<PlaylistTrack> iterator = pl_tr.iterator();
		int newOrder = 0;
		while(iterator.hasNext()) {
			PlaylistTrack e = iterator.next();
			if(e.getTrack().equals(track)) {
				playlistTrackRepository.delete(e);
				iterator.remove();
				break;
			}
			else {
				e.setTrackOrder(++newOrder);
			}
		}
		playlistRepository.save(playlist);
		return playlistMapper.toDto(playlist, true);
	}
	
	@Transactional
	public PlaylistDto createPlaylist(String name, User user) throws Exception {
		Playlist entity = new Playlist();
		entity.setId(null);
		entity.setCover(null);
		entity.setName(name);
		entity.setPrivate(false);
		entity.setTracks(new ArrayList<PlaylistTrack>());
		entity.setUser(user);
		
		Playlist result = playlistRepository.save(entity);

		return playlistMapper.toDto(result, true);
	}
	
	/*@Transactional
	public PlaylistDto removeTrackById(UUID trackId, UUID playlistId) throws NotFoundException {
		PlaylistTrack track = playlistTrackRepository.findByPlaylistIdAndTrackId(playlistId, trackId).orElseThrow(() -> new NotFoundException());
		return removeTrack(track.getTrack(), track.getPlaylist());
	}*/
	
	@Transactional
	public PlaylistDto removeTrackById(Track track, Playlist playlist) throws NotFoundException {
	    PlaylistTrack pt = playlistTrackRepository.findByPlaylistAndTrack(playlist, track)
	            .orElseThrow(() -> new NotFoundException());

	        playlistTrackRepository.delete(pt);

	        List<PlaylistTrack> tracks = playlistTrackRepository.findByPlaylist(playlist);
	        tracks.sort(Comparator.comparingInt(PlaylistTrack::getTrackOrder));
	        int order = 1;
	        for (PlaylistTrack t : tracks) {
	            t.setTrackOrder(order++);
	        }

	        playlistRepository.save(playlist);

	        return playlistMapper.toDto(playlist, true);

	}
	
	@Transactional
	public PlaylistDto changeCover(UUID imageId, UUID playlistId) throws Exception {
		Image image = imageRepository.findById(imageId).orElseThrow(() -> new NotFoundException());
		Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new NotFoundException());
		playlist.setCover(image);
		return playlistMapper.toDto(playlistRepository.save(playlist), true); 
	}
	
	@Transactional
	public void deletePlaylist(UUID playlistId) throws Exception {
		Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new NotFoundException());
		playlistRepository.delete(playlist);
	}
}
