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
import com.blacksabbath.lumitunespring.model.UserData;
import com.blacksabbath.lumitunespring.service.UserService;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUsers(HttpServletResponse response){
		Optional<List<User>> users = userService.getAllUsers();
		return users.map(ResponseEntity::ok)
				.orElseGet(()-> ResponseEntity.notFound().build());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> getById(@PathVariable String id, HttpServletResponse response){
		Optional<User> user = userService.getById(id);
		return user.map(ResponseEntity::ok)
				.orElseGet(()-> ResponseEntity.notFound().build());
	}
}
