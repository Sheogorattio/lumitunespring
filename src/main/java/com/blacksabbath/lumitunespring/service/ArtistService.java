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
import org.springframework.transaction.annotation.Transactional;

import com.blacksabbath.lumitunespring.dto.ArtistDto;
import com.blacksabbath.lumitunespring.mapper.AlbumMapper;
import com.blacksabbath.lumitunespring.mapper.ArtistMapper;
import com.blacksabbath.lumitunespring.mapper.ImageMapper;
import com.blacksabbath.lumitunespring.model.Album;
import com.blacksabbath.lumitunespring.model.Artist;
import com.blacksabbath.lumitunespring.model.Image;
import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.repository.ArtistRepository;

import jakarta.persistence.EntityNotFoundException;



@Service
public class ArtistService {
	
	private final ArtistRepository repository;
	
	private final AlbumMapper albumMapper;
	
	private final ArtistMapper artistMapper;
	
	private final ImageMapper imageMapper;
    
    public ArtistService(AlbumMapper albumMapper, ArtistRepository repository, ArtistMapper artistMapper, ImageMapper imageMapper) {
    	this.albumMapper = albumMapper;
    	this.repository= repository;
    	this.artistMapper = artistMapper;
    	this.imageMapper = imageMapper;
    }
	
	@Transactional
	public ArtistDto createArtist(Artist artist) {
		return artistMapper.toDto(repository.save(artist),true);
	}
	
	@Transactional
	public boolean deleteArtist(UUID id) {
		Artist artist = repository.findById(id).orElse(null);
		if(artist != null) {
			repository.delete(artist);
			return true;
		}
		return false;
	}
	 
	@Transactional
	public Optional<ArtistDto> findById(UUID id) { 
		return repository.findById(id).map(artist -> artistMapper.toDto(artist, true));
	}
	
	@Transactional(readOnly = true)
	public Optional<ArtistDto> findByUser(User user) {
		return repository.findByUser(user).map(artist -> artistMapper.toDto(artist, true));
	}
	
	@Transactional
	public ArtistDto updateArtist(UUID id, ArtistDto updatedArtistDto) {
		Artist existingArtist = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Artist with ID " + id + " not found"));
		
		existingArtist.setBio(updatedArtistDto.getBio());
		existingArtist.setMonthlyListeners(updatedArtistDto.getMonthlyListeners());
		existingArtist.setBioPics(Optional.ofNullable(updatedArtistDto.getBioPics())
		        .orElse(Collections.emptyList()) 
		        .stream()
		        .map(t -> {
		            try {
		                return imageMapper.toEntity(t);
		            } catch (Exception e) {
		                e.printStackTrace();
		                return null;
		            }
		        })
		        .filter(Objects::nonNull)
		        .collect(Collectors.toList()));
		existingArtist.setAlbums(Optional.ofNullable(updatedArtistDto.getAlbums())
				.orElse(Collections.emptyList())
				.stream().map(albumMapper::toEntity)
				.filter(Objects::nonNull)
				.collect(Collectors.toList()));
		Artist saved = repository.save(existingArtist);
		return artistMapper.toDto(saved,true);
	}
	
	@Transactional
	public ArtistDto addAlbumToArtist(UUID artistId, Album album) {
	    Artist artist = repository.findById(artistId)
	        .orElseThrow(() -> new EntityNotFoundException("Artist with ID " + artistId + " not found"));

	    album.setArtist(artist);
	    artist.getAlbums().add(album);

	    Artist saved = repository.save(artist);
	    return artistMapper.toDto(saved, true);
	}
	
	@Transactional
	public ArtistDto addBioPicToArtist(UUID artistId, Image image) {
	    Artist artist = repository.findById(artistId)
	        .orElseThrow(() -> new EntityNotFoundException("Artist with ID " + artistId + " not found"));

	    artist.getBioPics().add(image);

	    Artist saved = repository.save(artist);
	    return artistMapper.toDto(saved,true);
	}

	@Transactional(readOnly = true)
	public List<ArtistDto> findAll(){
		return repository.findAll().stream().map(artist -> artistMapper.toDto(artist, true)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly= true)
	public Optional<ArtistDto> findByUsername(String username) {
		return repository.findByUserUsername(username).map((e) -> artistMapper.toDto(e, true));
	}

}
