package com.blacksabbath.lumitunespring.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.model.UserData;
import com.blacksabbath.lumitunespring.repository.UserRepository;

import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceContext;

@Service
public class UserService {
	
	@Autowired 
	UserRepository userRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional(readOnly=true)
	public User findUserById(UUID id) {
		return userRepository.findById(id)
				.orElseThrow(()->new EntityNotFoundException("User not found: " + id));
	}
	
	@Transactional
	public User createUser(String username, String password, String avatarId, String dataId , String roleId , int accSubscribers,  int accFollowings, UserData data) {
		User newUser = new User (username,
								password,
								avatarId != null ? avatarId : null,
								data != null ? data : null,
								roleId != null ? roleId : null,
								accSubscribers != 0 ? accSubscribers : 0,
								accFollowings != 0 ? accFollowings : 0
								);
		return userRepository.save(newUser);
	}
	
	@Transactional
	public User createUser(User user) {
		return Optional.ofNullable(user)
				.map(userRepository::save)
				.orElseThrow(() -> new IllegalArgumentException("User must not be null!"));
	}
	
	 public User createUserWithUserData(User user) {
	        UserData userData = user.getUserData();
	        if (userData != null) {
	            userData.setUser(user);
	        }
	        return userRepository.save(user);
	    }
	
	@Transactional(readOnly=true)
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
	public Optional<List<User>> getAllUsers() {
	    List<User> users = userRepository.findAll();
	    return users.isEmpty() ? Optional.empty() : Optional.of(users);
	}
	
	@Transactional(readOnly=true)
	public boolean isNicknameUnique(String nickname) {
		if(nickname == null) {
			return false;
		}
		try {
			return !userRepository.findByUsername(nickname).isEmpty();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
}
