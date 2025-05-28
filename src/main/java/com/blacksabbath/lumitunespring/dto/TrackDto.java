package com.blacksabbath.lumitunespring.dto;

public class TrackDto {

    private String id;

    private String name;

    private ArtistDto artist;

    private int duration;

    private int segNumber;

    private int playsNumber;

    private boolean isExplicit;

    private AlbumDto album;
    
    private String url;

    public TrackDto() {}

    public TrackDto(String id, String name, ArtistDto artist, int duration, int segNumber, int playsNumber, boolean isExplicit, AlbumDto album, String url) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.duration = duration;
        this.segNumber = segNumber;
        this.playsNumber = playsNumber;
        this.isExplicit = isExplicit;
        this.album = album;
        this.url = url;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArtistDto getArtist() {
        return artist;
    }

    public void setArtist(ArtistDto artist) {
        this.artist = artist;
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

    public AlbumDto getAlbum() {
        return album;
    }

    public void setAlbum(AlbumDto album) {
        this.album = album;
    }
}
