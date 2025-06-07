package com.blacksabbath.lumitunespring.mapper;

import java.util.List;
import java.util.Objects;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Component;

import com.blacksabbath.lumitunespring.dto.PlaylistTrackDto;
import com.blacksabbath.lumitunespring.model.Playlist;
import com.blacksabbath.lumitunespring.model.PlaylistTrack;
import com.blacksabbath.lumitunespring.model.Track;
import com.blacksabbath.lumitunespring.service.PlaylistService;
import com.blacksabbath.lumitunespring.service.TrackService;

@Component
public class PlaylistTrackMapper {
	
	private final PlaylistMapper playlistMapper;
	private final TrackMapper trackMapper;
	private final PlaylistService playlistService;
	private final TrackService trackService;

	public PlaylistTrackMapper(PlaylistMapper playlistMapper,TrackMapper trackMapper, PlaylistService playlistService, TrackService trackService) {
		this.playlistMapper = playlistMapper;
		this.trackMapper = trackMapper;
		this.playlistService = playlistService;
		this.trackService = trackService;
	}
	
	public PlaylistTrackDto toDto(PlaylistTrack entity, boolean includeNested) {
		PlaylistTrackDto dto = new PlaylistTrackDto();
		dto.setId(entity.getId());
		dto.setPlaylistId(entity.getPlaylist().getId());
		dto.setTrackId(entity.getTrack().getId());
		dto.setTrackOrder(entity.getTrackOrder());
		return dto;
	}
	
	public PlaylistTrack toEntity(PlaylistTrackDto dto) throws Exception {
		PlaylistTrack entity = new PlaylistTrack();
		
		entity.setId(dto.getId());
		Playlist playlist = playlistService.findEntityPlaylistById(dto.getPlaylistId());
		entity.setPlaylist(playlist);
		Track track = trackService.findEntityById(dto.getTrackId());
		entity.setTrack(track);
		entity.setTrackOrder(dto.getTrackOrder());
		return entity;
	}
	
	public List<PlaylistTrack> toEntity(List<PlaylistTrackDto> dtos) throws Exception{
		return dtos.stream().map((e) -> {
			try {
				return toEntity(e);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		})
		.filter(Objects::nonNull)
		.toList(); 
	}
}
