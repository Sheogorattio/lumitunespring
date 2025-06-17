package com.blacksabbath.lumitunespring.mapper;

import com.blacksabbath.lumitunespring.dto.ArtistDto;
import com.blacksabbath.lumitunespring.model.Artist;
import com.blacksabbath.lumitunespring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ArtistMapper {


    private final UserRepository userRepository;
    
    private final ImageMapper imageMapper;

    private final AlbumMapper albumMapper;
    
    private final UserMapper userMapper;
    
    public ArtistMapper (UserRepository userRepository, @Lazy AlbumMapper albumMapper, ImageMapper imageMapper, UserMapper userMapper) {
    	this.userRepository = userRepository;
    	this.albumMapper = albumMapper;
    	this.imageMapper = imageMapper;
    	this.userMapper = userMapper;
    }

    public ArtistDto toDto(Artist artist, boolean  includeNested) {
        return new ArtistDto(
            artist.getId().toString(),
            userMapper.toDto(artist.getUser(), false),
            artist.getBio(),
            artist.getMonthlyListeners(),
            artist.getBioPics().stream().map(imageMapper::toDto).collect(Collectors.toList()),
            includeNested ? artist.getAlbums().stream().map(album -> albumMapper.toDto(album, false)).collect(Collectors.toList()) : List.of()
        );
    }

    public Artist toEntity(ArtistDto dto) {
        Artist artist = new Artist();
        artist.setId(java.util.UUID.fromString(dto.getId()));
        artist.setUser(userRepository.findById(java.util.UUID.fromString(dto.getUser().getId())).orElse(null));
        artist.setBio(dto.getBio());
        artist.setMonthlyListeners(dto.getMonthlyListeners());
        artist.setBioPics(dto.getBioPics().stream().map(t -> {
			try {
				return imageMapper.toEntity(t);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}).collect(Collectors.toList()));
        artist.setAlbums(Optional.ofNullable(dto.getAlbums()).orElse(null).stream().map(albumMapper::toEntity).filter(Objects::nonNull).toList());
        return artist;
    }
}
