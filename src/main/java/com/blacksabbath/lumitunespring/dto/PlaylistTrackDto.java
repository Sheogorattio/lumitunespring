package com.blacksabbath.lumitunespring.dto;

import java.util.UUID;

import com.blacksabbath.lumitunespring.model.Playlist;
import com.blacksabbath.lumitunespring.model.Track;

public class PlaylistTrackDto {
	private UUID id;
	private PlaylistDto playlist;
	private  TrackDto track;
	private int trackOrder;
	
	public PlaylistTrackDto() {
		
	}
	
	public PlaylistTrackDto(UUID id, PlaylistDto playlist, TrackDto track, int trackOrder) {
		this.id = id;
		this.playlist = playlist;
		this.track = track;
		this.trackOrder = trackOrder;
	}
	
	public UUID getId() {
		return this.id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public PlaylistDto getPlaylist() {
		return this.playlist;
	}
	
	public void setPlaylist(PlaylistDto playlist) {
		this.playlist = playlist;
	}
	
	public TrackDto getTrack() {
		return this.track;
	}
	
	public void setTrack(TrackDto track) {
		this.track = track;
	}
	
	public int getTrackOrder() {
		return this.trackOrder;
	}
	
	public void setTrackOrder(int trackOrder) {
		this.trackOrder = trackOrder;
	}
}
