package com.blacksabbath.lumitunespring.mapper;

import org.springframework.stereotype.Component;

import com.blacksabbath.lumitunespring.dto.PlaylistTrackDto;
import com.blacksabbath.lumitunespring.model.PlaylistTrack;

@Component
public class PlaylistTrackMapper {
	
	private final PlaylistMapper playlistMapper;
	private final TrackMapper trackMapper;

	public PlaylistTrackMapper(PlaylistMapper playlistMapper,TrackMapper trackMapper) {
		this.playlistMapper = playlistMapper;
		this.trackMapper = trackMapper;
	}
	
	public PlaylistTrackDto toDto(PlaylistTrack entity, boolean includeNested) {
		PlaylistTrackDto dto = new PlaylistTrackDto();
		dto.setId(entity.getId());
		dto.setPlaylist(playlistMapper.toDto(entity.getPlaylist(), false));
		dto.setTrack(trackMapper.toDto(entity.getTrack(), false));
		dto.setTrackOrder(entity.getTrackOrder());
		return dto;
	}
	
	public PlaylistTrack toEntity(PlaylistTrackDto dto) throws Exception {
		PlaylistTrack entity = new PlaylistTrack();
		
		entity.setId(dto.getId());
		entity.setPlaylist(playlistMapper.toEntity(dto.getPlaylist()));
		entity.setTrack(trackMapper.toEntity(dto.getTrack()));
		entity.setTrackOrder(dto.getTrackOrder());
		return entity;
	}
}
