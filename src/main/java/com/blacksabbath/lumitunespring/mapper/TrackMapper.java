package com.blacksabbath.lumitunespring.mapper;

import com.blacksabbath.lumitunespring.dto.TrackDto;
import com.blacksabbath.lumitunespring.model.Track;
import com.blacksabbath.lumitunespring.repository.ArtistRepository;
import com.blacksabbath.lumitunespring.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackMapper {

    @Autowired
    static ArtistRepository artistRepository;

    @Autowired
    private static AlbumRepository albumRepository;

    public static TrackDto toDto(Track track) {
        return new TrackDto(
            track.getId().toString(),
            track.getName(),
            ArtistMapper.toDto(track.getArtist()),
            track.getDuration(),
            track.getSegNumber(),
            track.getPlaysNumber(),
            track.isExplicit(),
            AlbumMapper.toDto(track.getAlbum())
        );
    }

    public static Track toEntity(TrackDto dto) {
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

        return track;
    }
}
