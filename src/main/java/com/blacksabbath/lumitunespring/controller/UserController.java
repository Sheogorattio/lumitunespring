package com.blacksabbath.lumitunespring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blacksabbath.lumitunespring.dto.UserDto;
import com.blacksabbath.lumitunespring.misc.AccessChecker;
import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.service.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUsers(HttpServletRequest request, HttpServletResponse response){
		
		if(!AccessChecker.Check(request, "")) {
			return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).build();
		}
		
		List<User> users = userService.getAllUsers();
		if(users.isEmpty()) {
			return ResponseEntity.noContent().build();
		} 
		return ResponseEntity.ok(users);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> getById(@PathVariable String id, HttpServletResponse response){
		Optional<User> user = userService.getById(id);
		return user.map(ResponseEntity::ok)
				.orElseGet(()-> ResponseEntity.notFound().build());
	}
	
	@PutMapping("/edit")
	public ResponseEntity<User> editById(@RequestBody UserDto userDto){
		return userService.editUserById(userDto)
						.map(ResponseEntity::ok)
						.orElse(ResponseEntity.notFound().build()); 
	}
}
