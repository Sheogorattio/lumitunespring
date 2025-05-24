package com.blacksabbath.lumitunespring.mapper;

import com.blacksabbath.lumitunespring.dto.AlbumDto;
import com.blacksabbath.lumitunespring.model.Album;
import com.blacksabbath.lumitunespring.repository.ArtistRepository;
import com.blacksabbath.lumitunespring.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AlbumMapper {

	@Autowired
    static ArtistRepository artistRepository;

    @Autowired
    static ImageRepository imageRepository;

    public static AlbumDto toDto(Album album) {
        return new AlbumDto(
            album.getId().toString(),
            album.getName(),
            ArtistMapper.toDto(album.getArtist()),
            album.getDuration(),
            album.getRelDate(),
            album.getType(),
            album.getLabel(),
            ImageMapper.toDto(album.getCover()),
            album.getTracks().stream().map(TrackMapper::toDto).collect(Collectors.toList())
        );
    }

    public static Album toEntity(AlbumDto dto) {
        Album album = new Album();
        album.setId(java.util.UUID.fromString(dto.getId()));
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
