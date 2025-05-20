package com.blacksabbath.lumitunespring.service;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;
import com.blacksabbath.lumitunespring.dto.ImageDto;
import com.blacksabbath.lumitunespring.mapper.ImageMapper;
import com.blacksabbath.lumitunespring.model.Image;
import com.blacksabbath.lumitunespring.repository.ImageRepository;
import com.blacksabbath.lumitunespring.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageBlobService {

	private final BlobContainerClient containerClient;
	
	@Autowired
	private ImageRepository repo;
	
	@Autowired
	private UserRepository userRepo;
	 
	public ImageBlobService(@Value("${AZURE_STORAGE_CONNSTR}") String connectionString) {
		String containerName = "images";
	    BlobServiceClient serviceClient = new BlobServiceClientBuilder()
	            .connectionString(connectionString)
	            .buildClient();
	    this.containerClient = serviceClient.getBlobContainerClient(containerName);
	    if (!this.containerClient.exists()) {
	        this.containerClient.create();
	    }
	}
	
	public ImageDto uploadImage(MultipartFile file, String ownerUsername) throws IOException {
		 
		
		String ext  = getExtension(file.getOriginalFilename());
	    String filename = UUID.randomUUID() + ext;
	    
	    BlobClient blobClient = containerClient.getBlobClient(filename);
	    blobClient.upload(file.getInputStream(), file.getSize(), true);
	    
	    return ImageMapper.toDto(repo.save(new Image(null, blobClient.getBlobUrl(), userRepo.findByUsername(ownerUsername).get())));
	}
	
	private String getExtension(String originalFilename) {
	    if (originalFilename != null && originalFilename.contains(".")) {
	        return originalFilename.substring(originalFilename.lastIndexOf("."));
	    }	        
	    return "";
	}
	
	public Optional<ImageDto> getById(String id) {
		Image image = repo.findById(UUID.fromString(id)).orElse(null);
		if(image == null) {
			return Optional.empty();
		}
		return Optional.of(ImageMapper.toDto(image));
	}
	
	public Optional<ImageDto> getByUrl(String Url) {
		Image image = repo.findByUrl(Url).orElse(null);
		if(image == null) {
			return Optional.empty();
		}
		return Optional.of(ImageMapper.toDto(image));
	}
	
	public Optional<ImageDto> update(ImageDto dto){
		Image image = repo.findById(UUID.fromString(dto.getId())).orElse(null);
		if(image == null) {
			return Optional.empty();
		}
		image.setUrl(dto.getId());
		return Optional.of(ImageMapper.toDto(repo.save(image)));
	}
	
	public void delete(String id) {
		repo.deleteById(UUID.fromString(id));
	}
}
