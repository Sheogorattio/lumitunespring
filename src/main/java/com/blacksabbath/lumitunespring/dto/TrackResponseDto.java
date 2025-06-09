package com.blacksabbath.lumitunespring.dto;

import java.util.UUID;

public class TrackResponseDto {
	  private String id;

	    private String name;

	    private UUID artistId;

	    private int duration;

	    private int segNumber;

	    private int playsNumber;

	    private boolean isExplicit;

	    private UUID albumId;
	    
	    private String url;
	    
	    private String coverUrl;

	    public TrackResponseDto() {}

	    public TrackResponseDto(String id, String name, UUID artistId, int duration, int segNumber, int playsNumber, boolean isExplicit, UUID albumId, String url, String coverUrl) {
	        this.id = id;
	        this.name = name;
	        this.artistId = artistId;
	        this.duration = duration;
	        this.segNumber = segNumber;
	        this.playsNumber = playsNumber;
	        this.isExplicit = isExplicit;
	        this.albumId = albumId;
	        this.url = url;
	        this.coverUrl = coverUrl;
	    }

	    public String getId() {
	        return id;
	    }

	    public void setId(String id) {
	        this.id = id;
	    }
	    
	    public String getUrl() {
	        return url;
	    }

	    public void setUrl(String url) {
	        this.url = url;
	    }
	    
	    public String getCoverUrl() {
	        return coverUrl;
	    }

	    public void setCoverUrl(String coverUrl) {
	        this.coverUrl = coverUrl;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public UUID getArtistId() {
	        return artistId;
	    }

	    public void setArtistId(UUID artistId) {
	        this.artistId = artistId;
	    }

	    public int getDuration() {
	        return duration;
	    }

	    public void setDuration(int duration) {
	        this.duration = duration;
	    }

	    public int getSegNumber() {
	        return segNumber;
	    }

	    public void setSegNumber(int segNumber) {
	        this.segNumber = segNumber;
	    }

	    public int getPlaysNumber() {
	        return playsNumber;
	    }

	    public void setPlaysNumber(int playsNumber) {
	        this.playsNumber = playsNumber;
	    }

	    public boolean isExplicit() {
	        return isExplicit;
	    }

	    public void setExplicit(boolean isExplicit) {
	        this.isExplicit = isExplicit;
	    }

	    public UUID getAlbumId() {
	        return albumId;
	    }

	    public void setAlbumId(UUID albumId) {
	        this.albumId = albumId;
	    }
}
