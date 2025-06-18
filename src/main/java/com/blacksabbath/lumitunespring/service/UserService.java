package com.blacksabbath.lumitunespring.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blacksabbath.lumitunespring.dto.UserDto;
import com.blacksabbath.lumitunespring.mapper.ImageMapper;
import com.blacksabbath.lumitunespring.mapper.PlaylistMapper;
import com.blacksabbath.lumitunespring.mapper.UserMapper;
import com.blacksabbath.lumitunespring.misc.Roles;
import com.blacksabbath.lumitunespring.model.Image;
import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.model.UserData;
import com.blacksabbath.lumitunespring.repository.ArtistRepository;
import com.blacksabbath.lumitunespring.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.swagger.v3.core.util.Json;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserMapper userMapper;
	
	@Autowired 
	ArtistRepository artistRepository;
	
	@Lazy
	@Autowired
	PlaylistService playlistService;
	
	@Lazy
	@Autowired
	PlaylistMapper playlistMapper;
	
	@Transactional(readOnly = true)
	public User findUserById(UUID id) {
		return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
	}

	@Transactional
	public User createUser(String username, String password, List<Image> avatar, Roles role, int accSubscribers,
			int accFollowings, UserData data) {
		User newUser = new User(username, password, avatar, data, role != null ? role : Roles.GUEST, accSubscribers,
				accFollowings, null);
		return userRepository.save(newUser);
	}

	@Transactional
	public User createUser(User user) throws JsonProcessingException {
		if (user == null) {
			System.out.println("User is null");
			throw new IllegalArgumentException("User must not be null");
		}
		UserData userData = user.getUserData();
		if (userData != null) {
			userData.setUser(user);
		}
		return userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public Optional<User> getById(String id) {
		if (id == null || id.isBlank()) {
			return Optional.empty();
		}
		try {
			UUID uuid = UUID.fromString(id);
			return userRepository.findById(uuid);
		} catch (IllegalArgumentException ex) {
			return Optional.empty();
		}
	}

	@Transactional(readOnly = true)
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Transactional(readOnly = true)
	public boolean isNicknameUnique(String nickname) {
		if (nickname == null) {
			return false;
		}
		try {
			return userRepository.findByUsername(nickname).isEmpty();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	@Transactional(readOnly = true)
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Transactional
	public Optional<User> editUserById(UserDto userDto) {
		if (userDto == null || userDto.getId() == null) {
			return Optional.empty();
		}
		return userRepository.findById(UUID.fromString(userDto.getId())).map(user -> {
			userMapper.updateEntity(user, userDto);
			return userRepository.save(user);
		});
	}
	
	@Transactional
	public void delete(User user) throws Exception {
		if(!userRepository.existsById(user.getId())) throw new Exception("User with ID '" + user.getId().toString() +"' does not exist.");
		if(user.getUserData().getIsArtist()) {
			artistRepository.delete(artistRepository.findByUser(user).orElseThrow(()-> new Exception("Unable to find artist by provided user with ID '" + user.getId().toString() +"'.")));
		}
		else {
			playlistService.findPlaylistsByUser(user).stream().filter(Objects::nonNull).forEach((e) -> {try {
				playlistService.deletePlaylist(playlistService.findEntityPlaylistById(e.getId()));
			} catch (Exception ex) {
				ex.printStackTrace();
				return;
			}});
			userRepository.delete(user);
		}
	}
	
	@Transactional(readOnly = true)
	public UserDto getCurrentUser(){
		try {
			Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(p == null) {
				throw new AuthenticationCredentialsNotFoundException("Unauthorized. Principal is not present.");
			}
			User user = ((User)p);
			user = getById(user.getId().toString()).orElse(null);
			if(user == null) {
				throw new AccessDeniedException("Invalid user data.");
			}
			return userMapper.toDto(user,true);
		}
		catch(Exception e){
			System.out.println("'UserController.current':"+e.getMessage());
			throw new RuntimeException();
		}
	} 
	
	
	@Transactional
	public UserDto subscribeTo(UUID subscriberId, UUID subscribeToId) throws NotFoundException {
		User subscriber = userRepository.findById(subscriberId).orElseThrow(() -> new NotFoundException());
		User subscribeTo = userRepository.findById(subscribeToId).orElseThrow(() -> new NotFoundException());
		subscriber.getSubscriptions().add(subscribeTo);
		subscriber.setAccFollowings(subscriber.getAccFollowings() + 1);
		
		subscribeTo.setAccSubscribers(subscribeTo.getAccSubscribers() +1);
		userRepository.save(subscribeTo);
		return userMapper.toDto(userRepository.save(subscriber), true);
	}
}
