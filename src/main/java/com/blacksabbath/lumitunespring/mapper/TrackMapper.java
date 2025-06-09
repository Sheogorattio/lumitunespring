package com.blacksabbath.lumitunespring.mapper;

import com.blacksabbath.lumitunespring.dto.TrackDto;
import com.blacksabbath.lumitunespring.dto.TrackResponseDto;
import com.blacksabbath.lumitunespring.model.Track;
import com.blacksabbath.lumitunespring.repository.ArtistRepository;
import com.blacksabbath.lumitunespring.repository.AlbumRepository;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class TrackMapper {

    private final ArtistRepository artistRepository;

    private final AlbumRepository albumRepository;
    
    private final AlbumMapper albumMapper;
    
    private final ArtistMapper artistMapper;
    
    public TrackMapper( ArtistRepository artistRepository,  AlbumRepository albumRepository, @Lazy AlbumMapper albumMapper, ArtistMapper artistMapper) {
    	this.artistRepository = artistRepository;
    	this.albumRepository = albumRepository;
    	this.albumMapper = albumMapper;
    	this.artistMapper = artistMapper;
    }

    public TrackDto toDto(Track track, boolean  includeNested) {
        return new TrackDto(
            track.getId().toString(),
            track.getName(),
            artistMapper.toDto(track.getArtist(),false),
            track.getDuration(),
            track.getSegNumber(),
            track.getPlaysNumber(),
            track.isExplicit(),
            albumMapper.toDto(track.getAlbum(),includeNested),
            track.getUrl()
        );
    }
    
    public TrackResponseDto toResponseDto(Track track) {
        return new TrackResponseDto( 
            track.getId().toString(),
            track.getName(),
            track.getArtist().getId(),
            track.getDuration(),
            track.getSegNumber(),
            track.getPlaysNumber(),
            track.isExplicit(),
            track.getAlbum().getId(),
            track.getUrl()
        );
    }
    
    public TrackResponseDto toResponseDto(TrackDto track) {
    	TrackResponseDto response = new TrackResponseDto();
    	response.setId(track.getId());
    	response.setName(track.getName());
    	response.setArtist(UUID.fromString(track.getArtist().getId()));
    	response.setDuration(track.getDuration());
    	response.setSegNumber(track.getSegNumber());
    	response.setExplicit(track.isExplicit());
    	response.setAlbum(track.getAlbum() != null ? UUID.fromString(track.getAlbum().getId()) : null);
    	response.setUrl(track.getUrl());
    	
    	return response;
    }

    public Track toEntity(TrackDto dto) {
        Track track = new Track();
        track.setId(java.util.UUID.fromString(dto.getId()));
        track.setName(dto.getName());
        track.setDuration(dto.getDuration());
        track.setSegNumber(dto.getSegNumber());
        track.setPlaysNumber(dto.getPlaysNumber());
        track.setExplicit(dto.isExplicit());

        if (dto.getArtist() != null) {
            track.setArtist(artistRepository.findById(java.util.UUID.fromString(dto.getArtist().getId())).orElse(null));
        }

        if (dto.getAlbum() != null) {
            track.setAlbum(albumRepository.findById(java.util.UUID.fromString(dto.getAlbum().getId())).orElse(null));
        }
        track.setUrl(dto.getUrl());

        return track;
    }
}
