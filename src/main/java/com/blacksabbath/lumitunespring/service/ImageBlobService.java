package com.blacksabbath.lumitunespring.service;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class ImageBlobService {

	 private final BlobContainerClient containerClient;

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

	    public String uploadImage(MultipartFile file) throws IOException {
	    	String ext  = getExtension(file.getOriginalFilename());
	        String filename = UUID.randomUUID() + ext;
	        
	        BlobClient blobClient = containerClient.getBlobClient(filename);
	        blobClient.upload(file.getInputStream(), file.getSize(), true);
	        return blobClient.getBlobUrl();
	    }

	    private String getExtension(String originalFilename) {
	        if (originalFilename != null && originalFilename.contains(".")) {
	            return originalFilename.substring(originalFilename.lastIndexOf("."));
	        }	        
	        return "";
	    }
}
