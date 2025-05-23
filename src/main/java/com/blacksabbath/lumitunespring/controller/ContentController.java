package com.blacksabbath.lumitunespring.controller;

import java.io.IOException;

import org.apache.maven.shared.utils.io.IOUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
@RequestMapping("/content")
@Tag(name = "Content", description = "Отримання контенту сторінки")
public class ContentController {
	@Value("classpath:data.json")
	Resource authorizedData;
	
	@Value("classpath:unauthorizedData.json")
	Resource unauthorizedData;
	
	@GetMapping("/main")
	public ResponseEntity<?> mainPage(HttpServletRequest request, HttpServletResponse response){
		
		try {
			return ResponseEntity.ok().body(IOUtil.toString(authorizedData.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build(); 
		}
	}
}
 