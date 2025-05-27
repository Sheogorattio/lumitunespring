package com.blacksabbath.lumitunespring.controller;

import com.blacksabbath.lumitunespring.dto.ArtistDto;
import com.blacksabbath.lumitunespring.mapper.ArtistMapper;
import com.blacksabbath.lumitunespring.mapper.UserMapper;
import com.blacksabbath.lumitunespring.misc.AccessChecker;
import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.repository.UserDataRepository;
import com.blacksabbath.lumitunespring.service.AlbumService;
import com.blacksabbath.lumitunespring.service.ArtistService;
import com.blacksabbath.lumitunespring.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/artists")
@Tag(name = "Artist related operations", description = "Операції над артистами")
public class ArtistController {

    private final AccessChecker accessChecker;

    private final ArtistService artistService;
    
    private final UserService userService;
    
    private final ArtistMapper artistMapper;
    
    @Autowired
	public ArtistController(AccessChecker accessChecker, ArtistService artistService, UserService userService, ArtistMapper artistMapper) {
		this.artistService = artistService;
		this.artistMapper = artistMapper;
		this.userService= userService;
		this.accessChecker = accessChecker;
	}

    @PostMapping
    public ResponseEntity<ArtistDto> createArtist(@RequestBody @Valid ArtistDto artistDto) {
        ArtistDto created = artistService.createArtist(artistMapper.toEntity(artistDto));
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDto> getArtist(@PathVariable UUID id) {
        Optional<ArtistDto> artist = artistService.findById(id);
        return artist.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    } 
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getArtistByUserId(@PathVariable UUID userId){
    	User user = userService.findUserById(userId);
    	if(user == null) {
    		return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body("Request user does not exist.");
    	}
    	
    	Optional<ArtistDto> artistDto = artistService.findByUser(user);
    	if (artistDto.isPresent()) {
            return ResponseEntity.ok(artistDto.get());
        } else {
            return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND)
                    .body("Artist linked to userId " + userId + " not found");
        } 
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArtist(@PathVariable UUID id, HttpServletRequest request) {
    	ArtistDto dto = artistService.findById(id).orElse(null);
    	if (dto == null) {
			return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body("Artist with id "+ id + " is not found.");
		}
    	UUID ownerId = UUID.fromString(dto.getUser().getId());
		if (!accessChecker.Check(request, ownerId)) {
			return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).build();
		}
        boolean deleted = artistService.deleteArtist(id);
        return deleted ? ResponseEntity.status(200).body("Artist with id " + id + " was deleted.") : ResponseEntity.status(404).body("Artist with id "+ id + " is not found."); 
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistDto> updateArtist(@PathVariable UUID id, @RequestBody @Valid ArtistDto updatedArtist, HttpServletRequest request) {
    	UUID ownerId = UUID.fromString(updatedArtist.getUser().getId());
		if (!accessChecker.Check(request, ownerId)) {
			return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).build();
		}
        ArtistDto updated = artistService.updateArtist(id, updatedArtist);
        return ResponseEntity.ok(updated);
    } 
    
    @GetMapping("/all")
    public ResponseEntity<List<ArtistDto>> getAll(){
    	List<ArtistDto> list = artistService.findAll();
    	return ResponseEntity.status(list.size() > 0 ? HttpServletResponse.SC_OK : HttpServletResponse.SC_NO_CONTENT ).body(list);
    } 
}
