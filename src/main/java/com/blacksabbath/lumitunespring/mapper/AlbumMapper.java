package com.blacksabbath.lumitunespring.mapper;

import com.blacksabbath.lumitunespring.dto.AlbumDto;
import com.blacksabbath.lumitunespring.dto.AlbumResponseDto;
import com.blacksabbath.lumitunespring.model.Album;
import com.blacksabbath.lumitunespring.repository.ArtistRepository;
import com.blacksabbath.lumitunespring.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AlbumMapper {

    private final ArtistRepository artistRepository;

    private final ImageRepository imageRepository;
    
    private final ArtistMapper artistMapper;
    
    private final TrackMapper trackMapper;
    
    private final ImageMapper imageMapper;
    
    public AlbumMapper(ArtistRepository artistRepository, ImageRepository imageRepository, ArtistMapper artistMapper, TrackMapper trackMapper, ImageMapper imageMapper) {
    	this.artistRepository = artistRepository;
        this.imageRepository = imageRepository;
        this.artistMapper = artistMapper;
        this.trackMapper = trackMapper;
        this.imageMapper = imageMapper;
    }

    public AlbumDto toDto(Album album, boolean includeNested) {
        return new AlbumDto(
            album.getId().toString(),
            album.getName(),
            artistMapper.toDto(album.getArtist(),false),
            album.getDuration(),
            album.getRelDate(),
            album.getType(),
            album.getLabel(),
            imageMapper.toDto(album.getCover()),
            includeNested && album.getTracks() !=null ? album.getTracks().stream().map(track -> trackMapper.toDto(track, false)).filter(Objects::nonNull).collect(Collectors.toList()) : List.of()
        );
    }
    
    public AlbumResponseDto toResponseDto(AlbumDto dto) {
    	AlbumResponseDto response = new AlbumResponseDto();
    	response.setId(dto.getId());
    	response.setArtistId(dto.getArtist().getId());
    	response.setCover(dto.getCover());
    	response.setDuration(dto.getDuration());
    	response.setLabel(dto.getLabel());
    	response.setName(dto.getName());
    	response.setRelDate(dto.getRelDate());
    	response.setTracks(dto.getTracks() == null && dto.getTracks().size() <= 0 ? null : dto.getTracks().stream().map(e -> trackMapper.toResponseDto(e)).toList());
    	response.setType(dto.getType());
    	return response;
    }
    
    public List<AlbumResponseDto> toResponseDto(List<AlbumDto> dtos) { 
    	return dtos.stream().map(e -> toResponseDto(e) ).toList();
    }

    public Album toEntity(AlbumDto dto) {
        Album album = new Album();
        album.setId(dto.getId() == null ? null : java.util.UUID.fromString(dto.getId()));
        album.setName(dto.getName());
        album.setDuration(dto.getDuration());
        album.setRelDate(dto.getRelDate());
        album.setType(dto.getType());
        album.setLabel(dto.getLabel());

        if (dto.getArtist() != null) {
            album.setArtist(artistRepository.findById(java.util.UUID.fromString(dto.getArtist().getId())).orElse(null));
        }

        if (dto.getCover() != null) {
            album.setCover(imageRepository.findById(java.util.UUID.fromString(dto.getCover().getId())).orElse(null));
        }

        return album;
    }
}
