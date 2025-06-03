package com.blacksabbath.lumitunespring.mapper;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.blacksabbath.lumitunespring.dto.PlaylistDto;
import com.blacksabbath.lumitunespring.model.Playlist;
import com.blacksabbath.lumitunespring.service.UserService;

@Component
public class PlaylistMapper {
	
	private final UserMapper userMapper;
	private final ImageMapper imageMapper;
	private final UserService userService;
	private final PlaylistTrackMapper playlistTrackMapper;
	
	public PlaylistMapper(UserMapper userMapper,  ImageMapper imageMapper, UserService userService, PlaylistTrackMapper playlistTrackMapper) {
		this.userMapper = userMapper;
		this.imageMapper = imageMapper;
		this.userService = userService;
		this.playlistTrackMapper = playlistTrackMapper;
	}
	
	public PlaylistDto toDto(Playlist entity, boolean includeNested) {
		PlaylistDto dto = new PlaylistDto();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setPrivate(entity.isPrivate());
		if(includeNested) {
			dto.setUser(userMapper.toDto(entity.getUser()));
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
		entity.setTracks(null);
		return entity;
	}
}
