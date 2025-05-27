package com.blacksabbath.lumitunespring.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blacksabbath.lumitunespring.dto.AlbumDto;
import com.blacksabbath.lumitunespring.mapper.AlbumMapper;
import com.blacksabbath.lumitunespring.mapper.ImageMapper;
import com.blacksabbath.lumitunespring.mapper.TrackMapper;
import com.blacksabbath.lumitunespring.model.Album;
import com.blacksabbath.lumitunespring.model.Artist;
import com.blacksabbath.lumitunespring.repository.AlbumRepository;

@Service
public class AlbumService {
	
    private final AlbumRepository repo;
    private final AlbumMapper albumMapper;
    private final TrackMapper trackMapper;

    @Autowired
    public AlbumService(AlbumRepository repo, AlbumMapper albumMapper, TrackMapper trackMapper) {
        this.repo = repo;
        this.albumMapper = albumMapper;
        this.trackMapper = trackMapper;
    }
	
	public Optional<AlbumDto> getAlbumById(UUID id) {
		return repo.findById(id).map(album -> albumMapper.toDto(album, true));
	}
	
	public List<AlbumDto> getByArtist(Artist artist){
		return repo.findByArtist(artist).stream().map(album -> albumMapper.toDto(album, false)).toList();
	}
	
	public AlbumDto createAlbum(AlbumDto album) {
		return albumMapper.toDto(repo.save(albumMapper.toEntity(album)),true);
	}
	
	public AlbumDto editAlbum(AlbumDto newAlbumDto) throws Exception {
		Album album = repo.findById(UUID.fromString(newAlbumDto.getId())).orElseThrow(() -> new Exception("Album with id "+ newAlbumDto.getId()+ " was not found."));
		
		album.setCover(ImageMapper.toEntity(newAlbumDto.getCover()));
		album.setLabel(newAlbumDto.getLabel());
		album.setRelDate(newAlbumDto.getRelDate());
		album.setType(newAlbumDto.getType());
		album.setDuration(newAlbumDto.getDuration());
		album.setTracks(Optional.ofNullable(newAlbumDto.getTracks()).orElse(Collections.emptyList()).stream().map(trackMapper::toEntity).filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new)));
		album.setName(newAlbumDto.getName());
		
		return albumMapper.toDto(repo.save(album),true);
	}
	
	public void deleteAlbum(UUID id) throws Exception {
		Album album = repo.findById(id).orElseThrow(() -> new Exception("Album with id "+ id.toString() + " was not found."));
		repo.delete(album);
	}
}
