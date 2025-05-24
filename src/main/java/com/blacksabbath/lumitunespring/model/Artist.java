package com.blacksabbath.lumitunespring.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name= "Artists")
public class Artist {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	private String bio;
	
	private int monthlyListeners;
	
	@OneToMany
	private List<Image> bioPics;
	
	@OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
	private List<Album> albums;
	
	
	public Artist() {}
	
	public Artist(UUID id, User user, String bio, int monthlyListeners, List<Image> bioPics, List<Album> albums ) {
		this.id = id;
		this.user = user;
		this.bio = bio;
		this.monthlyListeners = monthlyListeners;
		this.bioPics = bioPics;
		this.albums = albums;
	}
	
	public UUID getId() {
		return id;
	}
	
	public User getUser() {
		return user; 
	}
	
	public String getBio() {
		return bio;
	}
	
	public int getMonthlyListeners() {
		return monthlyListeners;
	}
	
	public List<Image> getBioPics(){
		return bioPics;
	}
	
	public List<Album> getAlbums(){
		return this.albums;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public void setUser(User user) {
		this.user = user; 
	}
	
	public void setBio(String bio) {
		this.bio = bio;
	}
	
	public void setMonthlyListeners(int monthlyListeners) {
		this.monthlyListeners = monthlyListeners;
	}
	
	public void setBioPics(List<Image> bioPics){
		this.bioPics = bioPics;
	}
	
	public void setAlbums(List<Album> albums){
		this.albums = albums;
	}
}
