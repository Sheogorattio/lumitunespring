package com.blacksabbath.lumitunespring.mapper;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import com.blacksabbath.lumitunespring.dto.PlaylistDto;
import com.blacksabbath.lumitunespring.dto.PlaylistResponseDto;
import com.blacksabbath.lumitunespring.model.Playlist;
import com.blacksabbath.lumitunespring.service.PlaylistTrackService;
import com.blacksabbath.lumitunespring.service.UserService;
import com.blacksabbath.lumitunespring.dto.TrackResponseDto;

@Component
public class PlaylistMapper {
	
	private final UserMapper userMapper;
	private final ImageMapper imageMapper;
	private final UserService userService;
	private final PlaylistTrackMapper playlistTrackMapper;
	private final TrackMapper trackMapper;
	private final PlaylistTrackService playlistTrackService;
	
	public PlaylistMapper(UserMapper userMapper,  ImageMapper imageMapper, UserService userService, @Lazy PlaylistTrackMapper playlistTrackMapper,TrackMapper trackMapper,PlaylistTrackService playlistTrackService) {
		this.userMapper = userMapper;
		this.imageMapper = imageMapper;
		this.userService = userService;
		this.playlistTrackMapper = playlistTrackMapper;
		this.trackMapper = trackMapper;
		this. playlistTrackService = playlistTrackService;
	}
	
	public PlaylistDto toDto(Playlist entity, boolean includeNested) {
		PlaylistDto dto = new PlaylistDto();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setPrivate(entity.isPrivate());
		if(includeNested) {
			dto.setUser(userMapper.toDto(entity.getUser(), false));
		}
		else {
			dto.setUser(null);
		}

		dto.setCover(imageMapper.toDto(entity.getCover()));
		dto.setTracks(entity.getTracks()
							.stream()
							.map(m -> playlistTrackMapper.toDto(m, false))
							.collect(Collectors.toList())
					);
		return dto;
	}
	
	public Playlist toEntity(PlaylistDto dto) throws Exception {
		Playlist entity = new Playlist();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setPrivate(dto.isPrivate());
		entity.setUser(userService.findUserById(UUID.fromString(dto.getUser().getId())));
		entity.setCover(imageMapper.toEntity(dto.getCover()));
		entity.setTracks(dto.getTracks().stream()
				.filter(Objects::nonNull)
				.map(t ->  {
					try {
						return playlistTrackMapper.toEntity(t);
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				})
				.filter(Objects::nonNull)
				.toList());
		return entity;
	}
	
	public PlaylistResponseDto toResponseDto(Playlist entity) {
		PlaylistResponseDto response = new PlaylistResponseDto();
		
		response.setId(entity.getId());
		response.setCoverUrl(entity.getCover() == null ? null : entity.getCover().getUrl());
		response.setIsPrivate(entity.isPrivate());
		response.setName(entity.getName());
		response.setUserName(entity.getUser().getUsername());
		response.setTracks(entity.getTracks().stream().map((pt)-> trackMapper.toResponseDto(pt.getTrack())).filter(Objects::nonNull).collect(Collectors.toList()));
		
		return response;
	}
	
	public PlaylistResponseDto toResponseDto(PlaylistDto dto) throws Exception {
		PlaylistResponseDto response = new PlaylistResponseDto();
		
		response.setId(dto.getId());
		response.setCoverUrl(dto.getCover() == null ? null : dto.getCover().getUrl());
		response.setIsPrivate(dto.isPrivate());
		response.setName(dto.getName());
		response.setUserName(dto.getUser().getUsername());
		
		List<TrackResponseDto> tracks = playlistTrackMapper.toEntity(dto.getTracks())
		.stream()
		.map((pltr) -> trackMapper.toResponseDto(pltr.getTrack()))
		.toList();
		
		response.setTracks(tracks);
		
		return response;
	}
}
