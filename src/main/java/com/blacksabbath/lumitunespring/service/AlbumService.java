package com.blacksabbath.lumitunespring.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import com.blacksabbath.lumitunespring.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blacksabbath.lumitunespring.dto.AlbumDto;
import com.blacksabbath.lumitunespring.dto.PlaylistDto;
import com.blacksabbath.lumitunespring.mapper.AlbumMapper;
import com.blacksabbath.lumitunespring.mapper.ImageMapper;
import com.blacksabbath.lumitunespring.mapper.TrackMapper;
import com.blacksabbath.lumitunespring.model.Album;
import com.blacksabbath.lumitunespring.model.Artist;
import com.blacksabbath.lumitunespring.model.Image;
import com.blacksabbath.lumitunespring.model.Playlist;
import com.blacksabbath.lumitunespring.model.Track;
import com.blacksabbath.lumitunespring.repository.AlbumRepository;
import com.blacksabbath.lumitunespring.repository.ImageRepository;

@Service
public class AlbumService {

    private final TrackRepository trackRepository;
	
    private final AlbumRepository repo;
    private final AlbumMapper albumMapper;
    private final TrackMapper trackMapper;
    private final ImageMapper imageMapper;
    private final ArtistService artistService;
    private final ImageRepository imageRepository;

    @Autowired
    public AlbumService(AlbumRepository repo, AlbumMapper albumMapper, TrackMapper trackMapper, ImageMapper imageMapper, ArtistService artistService, ImageRepository imageRepository, TrackRepository trackRepository) {
        this.repo = repo;
        this.albumMapper = albumMapper;
        this.trackMapper = trackMapper;
        this.imageMapper = imageMapper;
        this.artistService = artistService;
        this.imageRepository = imageRepository;
        this.trackRepository = trackRepository;
    }
	
	public Optional<AlbumDto> getAlbumById(UUID id) {
		return repo.findById(id).map(album -> albumMapper.toDto(album, true));
	}
	
	public List<AlbumDto> getByArtist(Artist artist){
		return repo.findByArtist(artist).stream().map(album -> albumMapper.toDto(album, false)).toList();
	}
	
	public AlbumDto createAlbum(AlbumDto albumDto) throws NotFoundException {
		Album album = new Album();
		album.setName(albumDto.getName());
		album.setLabel(albumDto.getLabel());
		album.setType(albumDto.getType());
		album.setDuration(0);
		album.setRelDate(albumDto.getRelDate());
		album.setTracks(new ArrayList<Track>());
		album.setCover(albumDto.getCover() != null && albumDto.getCover().getId() != null ? imageRepository.findById(UUID.fromString(albumDto.getCover().getId())).orElseThrow(() -> new NotFoundException()): null);
		album.setArtist(artistService.findEntityById(UUID.fromString(albumDto.getArtist().getId())).orElseThrow(() -> new NotFoundException()));
		
		Album _album = repo.save(album);
		System.out.println("CREATED ALBUM WITH ID: " + album.getId().toString());
		AlbumDto albumdto = albumMapper.toDto(_album,true);
		System.out.println("CREATED ALBUM DTO WITH ID: " + albumdto.getId().toString());
		return albumdto;
	}
	
	@Transactional
	public AlbumDto changeCover(UUID imageId, UUID albumId) throws Exception {
		Image image = imageRepository.findById(imageId).orElseThrow(() -> new NotFoundException());
		Album album = repo.findById(albumId).orElseThrow(() -> new NotFoundException());
		album.setCover(image);
		return albumMapper.toDto(repo.save(album), true); 
	}
	
	public AlbumDto editAlbum(AlbumDto newAlbumDto) throws Exception {
		Album album = repo.findById(UUID.fromString(newAlbumDto.getId())).orElseThrow(() -> new Exception("Album with id "+ newAlbumDto.getId()+ " was not found."));
		
		album.setCover(imageMapper.toEntity(newAlbumDto.getCover()));
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
