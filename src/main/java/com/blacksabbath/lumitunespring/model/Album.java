package com.blacksabbath.lumitunespring.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Album")
public class Album {

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
    private LocalDate relDate;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String label;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "cover_id")
    private Image cover;
    
    @OneToMany(mappedBy = "album", cascade = CascadeType.REMOVE)
    private List<Track> tracks;

    public Album() {}

    public Album(UUID id, String name, Artist artist, int duration, LocalDate relDate, String type, String label, Image cover, List<Track> tracks) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.duration = duration;
        this.relDate = relDate;
        this.type = type;
        this.label = label;
        this.cover = cover;
        this.tracks = tracks;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public LocalDate getRelDate() {
        return relDate;
    }

    public void setRelDate(LocalDate relDate) {
        this.relDate = relDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Image getCover() {
        return cover;
    }

    public void setCover(Image cover) {
        this.cover = cover;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
