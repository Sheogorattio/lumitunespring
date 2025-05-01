package com.blacksabbath.lumitunespring.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blacksabbath.lumitunespring.model.UserData;
import com.blacksabbath.lumitunespring.repository.UserDataRepository;


@Service
public class UserDataService {

	@Autowired
	UserDataRepository dataRepository;
	
	public UserData save(UserData userData) {
        return dataRepository.save(userData);
    }
	
	public List<UserData> findAll() {
        return dataRepository.findAll();
    }
	
	public Optional<UserData> findById(String id) {
        return dataRepository.findById(UUID.fromString(id));
    }
	
	public void deleteById(String id) {
        dataRepository.deleteById(UUID.fromString(id));
    }
	
	public boolean existsById(String id) {
        return dataRepository.existsById(UUID.fromString(id));
    }
}
