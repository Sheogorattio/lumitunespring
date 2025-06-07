package com.blacksabbath.lumitunespring.dto;

import java.util.UUID;

import com.blacksabbath.lumitunespring.model.Playlist;
import com.blacksabbath.lumitunespring.model.Track;

public class PlaylistTrackDto {
	private UUID id;
	private UUID playlistId;
	private  UUID trackId;
	private int trackOrder;
	
	public PlaylistTrackDto() {
		
	}
	
	public PlaylistTrackDto(UUID id, UUID playlistId, UUID trackId, int trackOrder) {
		this.id = id;
		this.playlistId = playlistId;
		this.trackId = trackId;
		this.trackOrder = trackOrder;
	}
	
	public UUID getId() {
		return this.id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public UUID getPlaylistId() {
		return this.playlistId;
	}
	
	public void setPlaylistId(UUID playlistId) {
		this.playlistId = playlistId;
	}
	
	public UUID getTrackId() {
		return this.trackId;
	}
	
	public void setTrackId(UUID trackId) {
		this.trackId = trackId;
	}
	
	public int getTrackOrder() {
		return this.trackOrder;
	}
	
	public void setTrackOrder(int trackOrder) {
		this.trackOrder = trackOrder;
	}
}
