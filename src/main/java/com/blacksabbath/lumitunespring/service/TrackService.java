package com.blacksabbath.lumitunespring.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.blacksabbath.lumitunespring.dto.TrackDto;
import com.blacksabbath.lumitunespring.mapper.TrackMapper;
import com.blacksabbath.lumitunespring.misc.FileOperations;
import com.blacksabbath.lumitunespring.misc.trackCreateRequest;
import com.blacksabbath.lumitunespring.repository.AlbumRepository;
import com.blacksabbath.lumitunespring.repository.ArtistRepository;
import com.blacksabbath.lumitunespring.repository.TrackRepository;
import com.blacksabbath.lumitunespring.repository.UserRepository;
import com.blacksabbath.lumitunespring.model.Album;
import com.blacksabbath.lumitunespring.model.Artist;
import com.blacksabbath.lumitunespring.model.Track;
import com.blacksabbath.lumitunespring.model.User;

@Service
public class TrackService {
	
	private final BlobContainerClient containerClient;
	
	private final TrackRepository trackRepo;
	
	private final TrackMapper trackMapper;
	
	private final UserRepository userRepo;
	
	private final ArtistRepository artistRepo;
	
	private final AlbumRepository albumRepo;
	
	@Autowired
	public TrackService(@Value("${AZURE_STORAGE_CONNSTR}") String connectionString, AlbumRepository albumRepo, TrackRepository trackRepo, TrackMapper trackMapper,UserRepository userRepo, ArtistRepository artistRepo) {
		String containerName = "audio";
		BlobServiceClient serviceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
		this.containerClient = serviceClient.getBlobContainerClient(containerName);
		if(!this.containerClient.exists()) {
			this.containerClient.create();
		}
		
		
		this.trackRepo = trackRepo;
		this.trackMapper = trackMapper;
		this.userRepo = userRepo;
		this.artistRepo = artistRepo;
		this.albumRepo = albumRepo;
	}
	

	@Transactional
	public TrackDto createTrack(MultipartFile file, trackCreateRequest trackReq) throws Exception {
		
		String ownerUsername = ((User)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUsername();
		Artist artist = artistRepo.findByUser(userRepo.findByUsername(ownerUsername).
				orElseThrow(() -> new Exception("Unable to assign track to the user with username '"+ownerUsername+"'. Possible cause: user does not exist."))
				)
				.orElseThrow(() -> new Exception("Unable to create track. Possible cause: current user is not an artist."));
		
		Album album = albumRepo.findById(UUID.fromString(trackReq.albumId)).
				orElseThrow(() -> new Exception("Album with id '"+trackReq.albumId+"' was not found. Possible cause: album does not exist."));
		
		String ext = FileOperations.getExtension(file.getOriginalFilename());
		String fileName = UUID.randomUUID().toString()+ext;
		
		BlobClient blobClient = containerClient.getBlobClient(fileName);
		blobClient.upload(file.getInputStream(), file.getSize(), true);
		
		
		
		Track track = new Track();
		track.setName(trackReq.name);
		track.setUrl(blobClient.getBlobUrl());
		track.setArtist(artist);
		track.setDuration(trackReq.duration);
		track.setSegNumber(album.getTracks().size()+1);
		track.setPlaysNumber(0);
		track.setExplicit(trackReq.isExplicit);
		track.setAlbum(album);
		track.setUrl(blobClient.getBlobUrl());
		
		return trackMapper.toDto(trackRepo.save(track), true);
	}

	@Transactional
	public TrackDto findById(UUID id) throws Exception { 
		Track track = trackRepo.findById(id).orElseThrow(()-> new Exception("Track with id '" + id.toString() +"' does not exist."));
		return trackMapper.toDto(track, false);
	}

	@Transactional
	public Track findEntityById(UUID id) throws Exception { 
		Track track = trackRepo.findById(id).orElseThrow(()-> new Exception("Track with id '" + id.toString() +"' does not exist."));
		return track;
	}
	
	@Transactional
	public List<TrackDto> findByName(String name){
		List<TrackDto> tracks = trackRepo.findByName(name).stream().filter(Objects::nonNull).map((t) -> trackMapper.toDto(t, false)).collect(Collectors.toList());
		return tracks;
	} 
	
	@Transactional
	public void delete(UUID id) {
		trackRepo.deleteById(id);
	}
	
	@Transactional
	public TrackDto addOneListening(UUID trackId) throws Exception {
		Track track = trackRepo.findById(trackId).orElseThrow(() -> new NotFoundException());
		track.setPlaysNumber(track.getPlaysNumber() +1);
		System.out.println("new plays number is " + track.getPlaysNumber());
		return trackMapper.toDto(trackRepo.save(track), true);
	}
}
