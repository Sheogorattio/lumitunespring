package com.blacksabbath.lumitunespring.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.blacksabbath.lumitunespring.service.PlaylistService;
import com.blacksabbath.lumitunespring.service.TrackService;
import com.blacksabbath.lumitunespring.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blacksabbath.lumitunespring.dto.PlaylistDto;
import com.blacksabbath.lumitunespring.dto.PlaylistResponseDto;
import com.blacksabbath.lumitunespring.dto.UserDto;
import com.blacksabbath.lumitunespring.mapper.PlaylistMapper;
import com.blacksabbath.lumitunespring.mapper.TrackMapper;
import com.blacksabbath.lumitunespring.model.Playlist;
import com.blacksabbath.lumitunespring.model.PlaylistTrack;
import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.model.Track;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final PlaylistMapper playlistMapper;
    private final UserService userService;
    private final TrackService trackService;
    private final TrackMapper trackMapper;

    PlaylistController(PlaylistService playlistService, PlaylistMapper playlistMapper,UserService userService, TrackService trackService, TrackMapper trackMapper) {
        this.playlistService = playlistService;
        this.playlistMapper = playlistMapper;
        this.userService = userService;
        this.trackService = trackService;
        this.trackMapper = trackMapper;
    }
    
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable UUID id) {
		
			try {
				PlaylistDto playlistDto = playlistService.findPlaylistById(id);
				return ResponseEntity.ok(playlistMapper.toResponseDto(playlistMapper.toEntity(playlistDto)));
			} 
			catch(NotFoundException e) {
				return ResponseEntity.notFound().build();
			} 
			catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.internalServerError().build();
			}
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<?> findByUserId(@PathVariable UUID id){
		try {
			List<PlaylistDto> playlists = playlistService.findPlaylistsByUserId(id);
			if(playlists == null || playlists.size() == 0 ) {
				return ResponseEntity.noContent().build(); 
			} 
			
			List<PlaylistResponseDto> result =playlists.stream()
														.map(t -> {
															try {
																return playlistMapper.toResponseDto(t);
															} catch (Exception e) {
																 throw new RuntimeException("Mapping error", e);
															}
														})
														.toList();
			
			return ResponseEntity.ok(result);
		}
		catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.internalServerError().build();
	    } 
	}
	
	
	@PostMapping("/")
	public ResponseEntity<?> create(@RequestBody CreateRequest name){
		PlaylistDto dto = null;
		try {
			UserDto userDto =userService.getCurrentUser();
			User user = userService.findUserById(UUID.fromString(userDto.getId()));
			dto = playlistService.createPlaylist(name.getName(), user);
		}
		catch(AccessDeniedException ex) {
			return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).body("Invalid user data.");
		}
		catch(AuthenticationCredentialsNotFoundException ex) {
			return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Unauthorized. Principal is not present.");
		}
		catch(RuntimeException ex) {
			return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).build();
		}
		catch(Exception ex) {
			return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).build();
		}
		
		return ResponseEntity.ok(dto); 
	}
	
	@PostMapping("/add-song")
	public ResponseEntity<?> addSong(@RequestBody AddRemoveSongRequest body){
		try {
			Track track = trackMapper.toEntity(trackService.findById(body.getSongId()));
			Playlist playlist = playlistMapper.toEntity(playlistService.findPlaylistById(body.getPlaylistId()));
			if(track != null && playlist != null) {
				PlaylistDto dto = playlistService.addTrack(track, playlist);
				PlaylistResponseDto result = playlistMapper.toResponseDto(dto);
				return ResponseEntity.ok(result);
			}
			else {
				return ResponseEntity.badRequest().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@PostMapping("/remove-song")
	public ResponseEntity<?> removeSong(@RequestBody AddRemoveSongRequest body){
	    try {
	    	Playlist playlist = playlistService.findEntityPlaylistById(body.getPlaylistId());
	    	Track track = trackService.findEntityById(body.getSongId());
	        PlaylistDto dto = playlistService.removeTrackById( track,playlist);
	        PlaylistResponseDto result = playlistMapper.toResponseDto(dto);
	        return ResponseEntity.ok(result);
	    } catch (NotFoundException ex) {
	        return ResponseEntity.notFound().build();
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return ResponseEntity.internalServerError().build(); 
	    }
	}
	
	@PostMapping("/set-cover")
	public ResponseEntity<?> setCover(@RequestParam UUID playlistId, @RequestParam UUID imageId){
		try {
            PlaylistDto dto = playlistService.changeCover( imageId, playlistId);
            return ResponseEntity.ok(playlistMapper.toResponseDto(dto));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
	}
	
	@DeleteMapping("/{playlistId}")
    public ResponseEntity<?> deletePlaylist(@PathVariable UUID playlistId) {
        try {
            playlistService.deletePlaylist(playlistId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
	
	@GetMapping("/favourites")
	public ResponseEntity<?> getFavourites(){
		try {
			UserDto userDto = userService.getCurrentUser();
			PlaylistDto playlistDto =  playlistService.getFavourites(UUID.fromString(userDto.getId()));
			return ResponseEntity.ok(playlistMapper.toResponseDto(playlistDto));
		}
		catch (NotFoundException ex){
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		} 
	}
	
}

class CreateRequest{
	private String name;
	
	public CreateRequest() {}
	public CreateRequest(String name) {this.name = name;}
	public String getName() {return this.name;}
	public void setName(String name) {this.name = name;}
}

class AddRemoveSongRequest{
	private UUID playlistId;
	private UUID songId;
	
	public AddRemoveSongRequest() {}
	public AddRemoveSongRequest(UUID playlistId, UUID songId) {
		this.playlistId = playlistId;
		this.songId = songId;
	}
	
	public UUID getPlaylistId() {return this.playlistId;}
	public void setPlaylistId(UUID playlistId) {this.playlistId = playlistId;}
	
	public UUID getSongId() {return this.songId;}
	public void setSongId(UUID songId) {this.songId = songId;}
}

class PlaylistCreateDto{
	private String name;
	private boolean isPrivate;
	
	public PlaylistCreateDto() {}
	public PlaylistCreateDto(UUID id, String name, boolean isPrivate) {
		
	}
}