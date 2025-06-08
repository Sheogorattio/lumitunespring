package com.blacksabbath.lumitunespring.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "playlists")
public class Playlist {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JoinColumn(name = "cover_id")
	private Image cover;
	
	@Column(nullable = false)
	private boolean isPrivate;
	
	@OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PlaylistTrack> tracks;
	 
	public Playlist() {}
	
	public Playlist( UUID id, User user, Image cover,  boolean isPrivate, List<PlaylistTrack> tracks) {
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

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Image getCover() {
		return cover;
	}
	
	public void setCover(Image cover) {
		this.cover = cover;
	}
	
	public boolean isPrivate() {
		return isPrivate;
	}
	
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	
	public List<PlaylistTrack> getTracks() {
		return tracks;
	}
	
	public void setTracks(List<PlaylistTrack> tracks) {
		this.tracks = tracks;
	}
}
