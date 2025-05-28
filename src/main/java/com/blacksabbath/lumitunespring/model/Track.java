package com.blacksabbath.lumitunespring.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "Track")
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private int segNumber;

    @Column(nullable = false)
    private int playsNumber;

    @Column(nullable = false)
    private boolean isExplicit;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;
    
    @Column(nullable = false)
    private String url;

    public Track() {}

    public Track(UUID id, String name, Artist artist, int duration, int segNumber, int playsNumber, boolean isExplicit, Album album, String url) {
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
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

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
