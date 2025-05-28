package com.blacksabbath.lumitunespring.controller;

import com.blacksabbath.lumitunespring.dto.TrackDto;
import com.blacksabbath.lumitunespring.misc.trackCreateRequest;
import com.blacksabbath.lumitunespring.model.Track;
import com.blacksabbath.lumitunespring.service.TrackService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tracks")
public class TrackController {
 
    private final TrackService trackService;

    @Autowired
    public TrackController(TrackService trackService) {
        this.trackService = trackService;
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
            return ResponseEntity.ok(track);
        } catch (Exception e) { 
            return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<TrackDto>> searchByName(@RequestParam("name") String name) {
        List<TrackDto> results = trackService.findByName(name);
        return ResponseEntity.ok(results);
    } 

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrack(@PathVariable UUID id) {
        trackService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
