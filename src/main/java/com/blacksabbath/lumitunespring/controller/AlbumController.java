package com.blacksabbath.lumitunespring.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blacksabbath.lumitunespring.dto.AlbumDto;
import com.blacksabbath.lumitunespring.dto.AlbumResponseDto;
import com.blacksabbath.lumitunespring.dto.PlaylistDto;
import com.blacksabbath.lumitunespring.mapper.AlbumMapper;
import com.blacksabbath.lumitunespring.mapper.ArtistMapper;
import com.blacksabbath.lumitunespring.service.AlbumService;
import com.blacksabbath.lumitunespring.service.ArtistService;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/albums")
@Tag(name = "Album related operations", description = "Операції над альбомами")
public class AlbumController {

	private final AlbumService albumService;
	
	private final ArtistService artistService;
	
	private final ArtistMapper artistMapper;
	
	private final AlbumMapper albumMapper;
	
	
	@Autowired
	public AlbumController(AlbumService albumService, ArtistService artistService, ArtistMapper artistMapper,  AlbumMapper albumMapper) {
		this.albumService = albumService;
		this.artistService = artistService;
		this.artistMapper = artistMapper;
		this.albumMapper = albumMapper;
	}

    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponseDto> getAlbumById(@PathVariable UUID id) {
        return albumService.getAlbumById(id).map(albumMapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AlbumResponseDto>> getAlbumsByArtist(@RequestParam UUID artistId) {
        return artistService.findById(artistId)
        		.map(artistMapper::toEntity)
        		.map(albumService::getByArtist)
        		.map(albumMapper::toResponseDto)
        		.map(ResponseEntity::ok)
        		.orElse(ResponseEntity.notFound().build()); 
    }

    @PostMapping
    public ResponseEntity<AlbumResponseDto> createAlbum(@RequestBody AlbumDto albumDto) {
        AlbumDto created;
        AlbumResponseDto response;
		try {
			created = albumService.createAlbum(albumDto);
			response = albumMapper.toResponseDto(created);
		} catch (NotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		} 
        return ResponseEntity
                .status(HttpServletResponse.SC_CREATED).body(response);
    }
     
	@PatchMapping("/set-cover")
	public ResponseEntity<?> setCover(@RequestParam UUID playlistId, @RequestParam UUID imageId){
		try {
            AlbumDto dto = albumService.changeCover( imageId, playlistId);
            return ResponseEntity.ok(albumMapper.toResponseDto(dto));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
	}

    @PutMapping("/{id}") 
    public ResponseEntity<?> updateAlbum(@PathVariable UUID id, @RequestBody AlbumDto albumDto) {
        if (!id.toString().equals(albumDto.getId())) {
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Id from path variable and from request body are not matching.");
        }

        try {
            AlbumDto updated = albumService.editAlbum(albumDto);
            return ResponseEntity.ok(albumMapper.toResponseDto(updated));
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable UUID id) {
        try {
            albumService.deleteAlbum(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
