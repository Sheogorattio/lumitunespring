package com.blacksabbath.lumitunespring.dto;

import java.util.List;
import java.util.UUID;

public class PlaylistDto {
	private UUID id;
	
	private String name;
	
	private UserDto user;
	
	private ImageDto cover;
	
	private boolean isPrivate;
	
	private List<PlaylistTrackDto> tracks;
	 
	public PlaylistDto() {}
	
	public PlaylistDto( UUID id, UserDto user, ImageDto cover,  boolean isPrivate, List<PlaylistTrackDto> tracks) {
		this.id = id;
		this.user = user;
		this.cover = cover;
		this.isPrivate = isPrivate;
		this.tracks = tracks;
	}
	
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public UserDto getUser() {
		return user;
	}
	
	public void setUser(UserDto user) {
		this.user = user;
	}
	
	public ImageDto getCover() {
		return cover;
	}
	
	public void setCover(ImageDto cover) {
		this.cover = cover;
	}
	
	public boolean isPrivate() {
		return isPrivate;
	}
	
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	
	public List<PlaylistTrackDto> getTracks() {
		return tracks;
	}
	
	public void setTracks(List<PlaylistTrackDto> tracks) {
		this.tracks = tracks;
	}
}
