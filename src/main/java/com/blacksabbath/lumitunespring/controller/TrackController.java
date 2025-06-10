package com.blacksabbath.lumitunespring.controller;

import com.blacksabbath.lumitunespring.dto.TrackDto;
import com.blacksabbath.lumitunespring.dto.TrackResponseDto;
import com.blacksabbath.lumitunespring.mapper.TrackMapper;
import com.blacksabbath.lumitunespring.misc.Genre;
import com.blacksabbath.lumitunespring.misc.Moods;
import com.blacksabbath.lumitunespring.misc.trackCreateRequest;
import com.blacksabbath.lumitunespring.model.Track;
import com.blacksabbath.lumitunespring.service.TrackService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tracks")
public class TrackController {
 
    private final TrackService trackService;
    
    private final TrackMapper trackMapper;

    @Autowired
    public TrackController(TrackService trackService,  TrackMapper trackMapper) {
        this.trackService = trackService;
        this.trackMapper = trackMapper;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createTrack(
            @RequestPart("file") MultipartFile file,
            @RequestPart("track") trackCreateRequest track) {
        try {
            TrackDto createdTrack = trackService.createTrack(file, track);
            return ResponseEntity.ok(createdTrack);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        } 
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTrackById(@PathVariable UUID id) {
        try {
            TrackDto track = trackService.findById(id);
            System.out.println("current plays number is " + track.getPlaysNumber());
            return ResponseEntity.ok(trackMapper.toResponseDto(track));
        } catch (Exception e) { 
            return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<TrackResponseDto>> searchByName(@RequestParam("name") String name) {
        List<TrackDto> results = trackService.findByName(name);
        return ResponseEntity.ok(results.stream().map( e -> trackMapper.toResponseDto(e)).collect(Collectors.toList()));
    } 

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrack(@PathVariable UUID id) {
        trackService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/add-listening/{songId}")
    public ResponseEntity<?> addListening(@PathVariable UUID songId){
    	try {
    		trackService.addOneListening(songId);
    		return ResponseEntity.ok().build();
    	}
    	catch(NotFoundException e) {
    		return ResponseEntity.notFound().build();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		return ResponseEntity.internalServerError().build();
    	}
    }
    
    @PatchMapping("/set-genres")
    public ResponseEntity<?> setGenres(@RequestBody SetGenresRequest body){
    	try {
    		List<Genre> genres = new ArrayList<>(body.getGenres().stream()
    				.map(Genre::valueOf)
    				.toList());
    		TrackDto dto = trackService.setGenres(body.getSongId(), genres);
			return ResponseEntity.ok(trackMapper.toResponseDto(dto));
		} catch (NotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
    	catch(Exception ex) {
    		ex.printStackTrace();
    		return ResponseEntity.internalServerError().build();
    	}
    }
    
    @PatchMapping("/set-mooods")
    public ResponseEntity<?> setMoods(@RequestBody SetMoodsRequest body){
    	try {
    		List<Moods> moods = new ArrayList<>(body.getMoods().stream()
    				.map(Moods::valueOf)
    				.toList());
    		TrackDto dto = trackService.setMoods(body.getSongId(), moods);
			return ResponseEntity.ok(trackMapper.toResponseDto(dto));
		} catch (NotFoundException e) {
			return ResponseEntity.notFound().build();
		} 
    	catch(Exception ex) {
    		ex.printStackTrace();
    		return ResponseEntity.internalServerError().build();
    	}
    } 
    
    @GetMapping("/mood/{mood}")
    public ResponseEntity<?> getByMood(@PathVariable String mood){
    	try {
    		List<TrackDto> dtos = trackService.findByMood(Moods.valueOf(mood));
    		if(dtos.size() > 0) {
    			return ResponseEntity.ok(dtos.stream().map(e -> trackMapper.toResponseDto(e)).toList());
    		}
    		return ResponseEntity.noContent().build();
    	}
    	catch(Exception ex) {
    		ex.printStackTrace();
    		return ResponseEntity.internalServerError().build();
    	}
    } 
    
    @GetMapping("/genre/{genre}")
    public ResponseEntity<?> getByGenre(@PathVariable String genre){
    	try {
    		List<TrackDto> dtos = trackService.findByGenre(Genre.valueOf(genre));
    		if(dtos.size() > 0) {
    			return ResponseEntity.ok(dtos.stream().map(e -> trackMapper.toResponseDto(e)).toList());
    		}
    		return ResponseEntity.noContent().build();
    	}
    	catch(Exception ex) {
    		ex.printStackTrace();
    		return ResponseEntity.internalServerError().build();
    	}
    }
}

class SetGenresRequest{
	private UUID songId;
	private List<String> genres;
	
	public SetGenresRequest(UUID songId, List<String> genres) {
		this.songId = songId;
		this.genres = genres;
	}
	
	public SetGenresRequest() {};
	
	public UUID getSongId() {return this.songId;}
	public void setSongId(UUID songId) {this.songId = songId;}
	
	public List<String> getGenres() {return this.genres;}
	public void setGenres(List<String> genres) {this.genres = genres;}
}

class SetMoodsRequest{
	private UUID songId;
	private List<String> moods;
	
	public SetMoodsRequest(UUID songId, List<String> moods) {
		this.songId = songId;
		this.moods = moods;
	}
	
	public SetMoodsRequest() {};
	
	public UUID getSongId() {return this.songId;}
	public void setSongId(UUID songId) {this.songId = songId;}
	
	public List<String> getMoods() {return this.moods;}
	public void setMoods(List<String> moods) {this.moods = moods;}
}
