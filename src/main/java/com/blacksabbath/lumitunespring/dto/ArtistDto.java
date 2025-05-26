package com.blacksabbath.lumitunespring.dto;

import java.util.ArrayList;
import java.util.List;


public class ArtistDto {

	private String id;

	private UserDto user;
	
	private String bio;
	
	private int monthlyListeners;
	
	private List<ImageDto> bioPics;
	
	private List<AlbumDto> albums;
	
	public ArtistDto() {}
	
	public ArtistDto(String id, UserDto user, String bio, int monthlyListeners, List<ImageDto> bioPics, List<AlbumDto> albums ) {
		this.id = id;
		this.user = user;
		this.bio = bio;
		this.monthlyListeners = monthlyListeners;
		this.bioPics = bioPics == null ? new ArrayList<>(): bioPics;
		this.albums = albums;
	}
	
	public String getId() {
		return id;
	}
	
	public UserDto getUser() {
		return user; 
	}
	
	public String getBio() {
		return bio;
	}
	
	public int getMonthlyListeners() {
		return monthlyListeners;
	}
	
	public List<ImageDto> getBioPics(){
		return bioPics;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setUser(UserDto user) {
		this.user = user; 
	}
	
	public void setBio(String bio) {
		this.bio = bio;
	}
	
	public void setMonthlyListeners(int monthlyListeners) {
		this.monthlyListeners = monthlyListeners;
	}
	
	public void setBioPics(List<ImageDto> bioPics){
		this.bioPics = bioPics == null ? new ArrayList<>(): bioPics;
	}
	
	public List<AlbumDto> getAlbums() {
		return albums;
	}

	public void setAlbums(List<AlbumDto> albums) {
		this.albums = albums;
	}
}
