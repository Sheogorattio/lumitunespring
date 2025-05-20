package com.blacksabbath.lumitunespring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.blacksabbath.lumitunespring.dto.ImageDto;
import com.blacksabbath.lumitunespring.misc.AccessChecker;
import com.blacksabbath.lumitunespring.security.JwtUtil;
import com.blacksabbath.lumitunespring.service.ImageBlobService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.*;

@RestController
@RequestMapping("/images")
@CrossOrigin
public class ImageController {

	private final ImageBlobService azureBlobService;

	private final JwtUtil jwtUtil;

	private final AccessChecker accessChecker;

	public ImageController(ImageBlobService azureBlobService, JwtUtil jwtUtil, AccessChecker accessChecker) {
		this.azureBlobService = azureBlobService;
		this.jwtUtil = jwtUtil;
		this.accessChecker = accessChecker;
	}

	@PostMapping("/api/upload")
	public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Cookie cookie = null;
			for (Cookie c : request.getCookies()) {
				if (c.getName().equals("jwt")) {
					cookie = c;
					break;
				}
			}
			String ownerName = null;
			if (cookie != null && cookie.getValue() != null) {
				ownerName = jwtUtil.getSubject(cookie.getValue());
				ImageDto image = azureBlobService.uploadImage(file, ownerName);
				return ResponseEntity.ok(image);
			} else {
				throw new Exception("invalid token");
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getImage(@RequestParam String id, HttpServletRequest request,
			HttpServletResponse response) {
		ImageDto image = azureBlobService.getById(id).orElse(null);
		if (image == null) {
			return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).build();
		}
		return ResponseEntity.ok(image);
	}

	@PostMapping("/api/delete/{id}")
	public ResponseEntity<?> delete(@RequestParam String id, HttpServletRequest request, HttpServletResponse response) {
		UUID ownerId = azureBlobService.getById(id).map(p -> UUID.fromString(p.getOwner())).orElse(null);
		if (!accessChecker.Check(request, ownerId)) {
			return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).build();
		}
		ImageDto image = azureBlobService.getById(id).orElse(null);
		if (image == null) {
			return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).build();
		}
		azureBlobService.delete(id);
		return ResponseEntity.ok(image);
	}

	@PutMapping("/api/update")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> update(@RequestBody ImageDto dto, HttpServletRequest request) {
		ImageDto res = azureBlobService.update(dto).orElse(null);
		if (res == null) {
			return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok(res);
	}

}