package com.blacksabbath.lumitunespring.mapper;

import com.blacksabbath.lumitunespring.dto.ArtistDto;
import com.blacksabbath.lumitunespring.model.Artist;
import com.blacksabbath.lumitunespring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ArtistMapper {

    @Autowired
    static UserRepository userRepository;

    public static ArtistDto toDto(Artist artist) {
        return new ArtistDto(
            artist.getId().toString(),
            UserMapper.toDto(artist.getUser()),
            artist.getBio(),
            artist.getMonthlyListeners(),
            artist.getBioPics().stream().map(ImageMapper::toDto).collect(Collectors.toList()),
            artist.getAlbums().stream().map(AlbumMapper::toDto).collect(Collectors.toList())
        );
    }

    public static Artist toEntity(ArtistDto dto) {
        Artist artist = new Artist();
        artist.setId(java.util.UUID.fromString(dto.getId()));
        artist.setUser(userRepository.findById(java.util.UUID.fromString(dto.getUser().getId())).orElse(null));
        artist.setBio(dto.getBio());
        artist.setMonthlyListeners(dto.getMonthlyListeners());
        artist.setBioPics(dto.getBioPics().stream().map(t -> {
			try {
				return ImageMapper.toEntity(t);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}).collect(Collectors.toList()));
        return artist;
    }
}
