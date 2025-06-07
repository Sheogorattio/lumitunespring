package com.blacksabbath.lumitunespring.dto;

import java.util.List;
import java.util.UUID;

public class PlaylistResponseDto {

	private UUID id;
	private String name;
	private String userName;
	private String coverUrl;
	private boolean isPrivate;
	private List<TrackResponseDto> tracks;
	
	public PlaylistResponseDto() {
		
	}
	public PlaylistResponseDto(UUID id, String name, String userName, String coverUrl, boolean isPrivate, List<TrackResponseDto> tracks) {
		this.id = id;
		this.name = name;
		this.userName = userName;
		this.coverUrl = coverUrl;
		this.isPrivate = isPrivate;
		this.tracks = tracks;
	}
	
	public UUID getId() {
		return this.id;
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
	
	public String getUserName() {
		return this.userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getCoverUrl() {
		return this.coverUrl;
	}
	
	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}
	
	public boolean isPrivate() {
		return this.isPrivate;
	}
	
	public void setIsPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	
	public List<TrackResponseDto> getTracks(){
		return tracks;
	}
	
	public void setTracks(List<TrackResponseDto> tracks) {
		this.tracks = tracks;
	}
}
