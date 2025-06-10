package com.blacksabbath.lumitunespring.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blacksabbath.lumitunespring.misc.Moods;

@RestController
@RequestMapping("/mood")
public class MoodController {

	@GetMapping
	public ResponseEntity<?> getAllGenres(){
		return ResponseEntity.ok(List.of(Moods.values()));
	}
}
