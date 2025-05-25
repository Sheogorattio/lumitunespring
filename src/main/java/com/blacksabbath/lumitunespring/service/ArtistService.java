package com.blacksabbath.lumitunespring.service;

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
import com.blacksabbath.lumitunespring.model.Artist;
import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.repository.ArtistRepository;

import jakarta.persistence.EntityNotFoundException;



@Service
public class ArtistService {
	
	@Autowired 
	ArtistRepository repository;
	
	@Transactional
	public ArtistDto createArtist(Artist artist) {
		return ArtistMapper.toDto(repository.save(artist));
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
		return repository.findById(id).map(ArtistMapper::toDto);
	}
	
	@Transactional(readOnly = true)
	public Optional<ArtistDto> findByUser(User user) {
		return repository.findByUser(user).map(ArtistMapper::toDto);
	}
	
	@Transactional
	public ArtistDto updateArtist(UUID id, ArtistDto updatedArtistDto) {
		Artist existingArtist = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Artist with ID " + id + " not found"));
		
		existingArtist.setBio(updatedArtistDto.getBio());
		existingArtist.setMonthlyListeners(updatedArtistDto.getMonthlyListeners());
		existingArtist.setBioPics(updatedArtistDto.getBioPics().stream().map(t -> {
			try {
				return ImageMapper.toEntity(t);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}).collect(Collectors.toList()));
		existingArtist.setAlbums(updatedArtistDto.getAlbums().stream().map(AlbumMapper::toEntity).collect(Collectors.toList()));
		Artist saved = repository.save(existingArtist);
		return ArtistMapper.toDto(saved);
	}
}
