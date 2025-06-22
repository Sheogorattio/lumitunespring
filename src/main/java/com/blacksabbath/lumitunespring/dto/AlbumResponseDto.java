package com.blacksabbath.lumitunespring.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class AlbumResponseDto {

    private String id;

    private String name;

    private String artistId;

    private int duration;

    private LocalDate relDate;

    private String type;

    private String label;

    private ImageDto cover;

    private List<TrackDto> tracks;

    public AlbumResponseDto() {}

    public AlbumResponseDto(String id, String name, String artistId, int duration, LocalDate relDate, String type, String label, ImageDto cover, List<TrackDto> tracks) {
        this.id = id;
        this.name = name;
        this.artistId = artistId;
        this.duration = duration;
        this.relDate = relDate;
        this.type = type;
        this.label = label;
        this.cover = cover;
        this.tracks = tracks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
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

    public ImageDto getCover() {
        return cover;
    }

    public void setCover(ImageDto cover) {
        this.cover = cover;
    }

    public List<TrackDto> getTracks() {
        return tracks;
    }

    public void setTracks(List<TrackDto> tracks) {
        this.tracks = tracks;
    }
}
