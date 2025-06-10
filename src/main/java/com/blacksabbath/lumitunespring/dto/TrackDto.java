package com.blacksabbath.lumitunespring.dto;

import java.util.List;

import com.blacksabbath.lumitunespring.misc.Genre;
import com.blacksabbath.lumitunespring.misc.Moods;

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
    
    private List<Genre> genres;
    
    private List<Moods> moods;

    public TrackDto() {}

    public TrackDto(String id, String name, ArtistDto artist, int duration, int segNumber, int playsNumber, boolean isExplicit, AlbumDto album, String url, List<Genre> genres, List<Moods> moods) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.duration = duration;
        this.segNumber = segNumber;
        this.playsNumber = playsNumber;
        this.isExplicit = isExplicit;
        this.album = album;
        this.url = url;
        this.genres= genres;
        this.moods = moods;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public List<Genre> getGenres(){
    	return this.genres;
    }
    
    public void setGenres(List<Genre> genres) {
    	this.genres = genres;
    }
    
    public List<Moods> getMoods(){
    	return this.moods;
    }
    
    public void setMoods(List<Moods> moods) {
    	this.moods = moods;
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
