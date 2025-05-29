package com.blacksabbath.lumitunespring.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "playlist_track", indexes = @Index(columnList = "playlist_id"))
public class PlaylistTrack {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@ManyToOne
	@JoinColumn(name ="playlist_id", nullable = false)
	private Playlist playlist;
	
	@ManyToOne
	@JoinColumn(name = "track_id", nullable = false)
	private Track track;
	
	@Column(name = "track_order", nullable = false)
	private int trackOrder;
	
	public PlaylistTrack() {}
	
	public PlaylistTrack(UUID id, Playlist playlist, Track track, int trackOrder) {
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
	
	public Playlist getPlaylist() {
		return this.playlist;
	}
	
	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}
	
	public Track getTrack() {
		return this.track;
	}
	
	public void setTrack(Track track) {
		this.track = track;
	}
	
	public int getTrackOrder() {
		return this.trackOrder;
	}
	
	public void setTrackOrder(int trackOrder) {
		this.trackOrder = trackOrder;
	}
}
