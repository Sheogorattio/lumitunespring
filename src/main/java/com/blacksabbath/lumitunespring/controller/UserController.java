package com.blacksabbath.lumitunespring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.service.UserService;
import java.net.URI;
import java.util.Optional;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/users")
@CrossOrigin
public class UserController {
	@Autowired
	private UserService userService;
	
	
	@PostMapping("/sign-up")
	public ResponseEntity<User> createUser(@RequestBody User user, HttpServletResponse response){
		User createdUser = userService.createUser(user);
		return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> getById(@PathVariable String id, HttpServletResponse response){
		Optional<User> user = userService.getById(id);
		return user.map(ResponseEntity::ok)
				.orElseGet(()-> ResponseEntity.notFound().build());
	}
}
