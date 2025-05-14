package com.blacksabbath.lumitunespring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.blacksabbath.lumitunespring.service.ImageBlobService;

@RestController
@RequestMapping("/images")
@CrossOrigin
public class ImageController {

    private final ImageBlobService azureBlobService;

	public ImageController(ImageBlobService azureBlobService) {
        this.azureBlobService = azureBlobService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = azureBlobService.uploadImage(file);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }
}

