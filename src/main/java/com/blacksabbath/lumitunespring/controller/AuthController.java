package com.blacksabbath.lumitunespring.controller;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blacksabbath.lumitunespring.mapper.RegisterRequestMapper;
import com.blacksabbath.lumitunespring.misc.LoginRequestBody;
import com.blacksabbath.lumitunespring.misc.RegisterRequestBody;
import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.security.JwtUtil;
import com.blacksabbath.lumitunespring.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/auth")
@CrossOrigin
@Tag(name = "Authentication", description = "Операції авторизації та аутентифікації")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtil jwt;
	
	@PostMapping("/sign-up")
	@Operation(
	    summary = "Реєстрація нового користувача",
	    description = "Створює нового користувача з переданими даними"
	)
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "201",
    				description = "Користувача створено успішно",
					content = @Content(
			                mediaType = "application/json",
			                schema = @Schema(implementation = User.class),
			                examples = @ExampleObject(
			                    name = "userExample",
			                    summary = "Приклад відповіді на коректний запит",
			                    value = """
			                    {
			                        "id": "6fa459ea-ee8a-3ca4-894e-db77e160355e",
			                        "username": "john_doe1",
			                        "password": "pass1234",
			                        "avatarId": "avatar123",
			                        "role": "USER",
			                        "accSubscribers": 10,
			                        "accFollowings": 5,
			                        "userData": {
			                            "birthDate": "1990-01-01",
			                            "regionId": "region001",
			                            "isArtist": true,
			                            "email": "john.doe@example.com"
			                        }
			                    }
			                    """
			                )
			            )
	    ),
	    @ApiResponse(
	    	    responseCode = "400",
	    	    description = "Некоректні дані користувача",
	    	    content = @Content(
	    	        mediaType = "application/json",
	    	        schema = @Schema(implementation = Map.class),
	    	        examples = @ExampleObject(
	    	            name = "errorResponse",
	    	            summary = "Приклад відповіді при помилці",
	    	            value = """
	    	            {
	    	                "message": "Nickname already exists"
	    	            }
	    	            """
	    	        )
	    	    )
	    	)
	})
	public ResponseEntity<?> createUser(@RequestBody RegisterRequestBody user, HttpServletResponse response) {
		if(!userService.isNicknameUnique(user.getUsername())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Nickname already exists"));
	    User createdUser = userService.createUserWithUserData(RegisterRequestMapper.toUserEntity(user));
	    return ResponseEntity
	            .status(HttpStatus.CREATED)
	            .body(createdUser);
	} 
	
	@PostMapping("/login")
	@Operation(
        summary = "Увійти",
        description = "Аутентифікує користувача за логіном і паролем та повертає JWT"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
        			description = "Успішна аутентифікація",
        			content = @Content(
        					mediaType = "application/json",
        					schema = @Schema(implementation = Map.class),
        					examples = @ExampleObject(
        							name ="Successful login",
        							summary = "Приклад відповіді при успішному логіні",
        							value = """
        									{
											  "user": {
											    "username": "john_doe1",
											    "password": "pass1234"
											  },
											  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huX2RvZTEiLCJpYXQiOjE3NDY1NTc2NzYsImV4cCI6MTc0NjU2MTI3Nn0.jed1Q5NT3E8CWdex-g5miIMADCmaA_SOnJrstVe5loA"
											}
        									"""
        							)
        					)
        ),
        @ApiResponse(responseCode = "401", 
        			description = "Невірне ім’я користувача або пароль",
        			content = @Content(
        					mediaType = "application/json",
        					schema = @Schema(implementation = Map.class),
        					examples = @ExampleObject(
        							name ="Failed login",
        							summary = "Приклад відповіді при провальному логіні",
        							value = """
        									"Invalid credentials"
        									"""
        							)
        					)
        )
    })
	public ResponseEntity<?> login(@RequestBody LoginRequestBody user){
		try {
			boolean doesExists = userService.findUserByUsername(user.getUsername())
									    	.map(u -> u.getPassword().equals(user.getPassword()))
										    .orElse(false);
			if(!doesExists) throw new Exception("Invalid credentials");
		}
		catch(Exception e) {
			System.out.println(this.getClass().getName() + ":login :" +  e.getMessage());
			return ResponseEntity.status(401).body("Invalid credentials");
		}
		String token = jwt.genarateToken(user.getUsername());
		return ResponseEntity.ok(Map.of("token", token, "user", user));
	}
	
	@GetMapping("/isunique/{nickname}")
	@Operation(
	    summary = "Перевірити унікальність нікнейму",
	    description = "Повертає true, якщо нікнейм ще не використовується"
	)
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Результат перевірки унікальності")
	})
	public ResponseEntity<Boolean> isNicknameUnique (@PathVariable String nickname, HttpServletResponse response){
		return ResponseEntity.ok(userService.isNicknameUnique(nickname));
	}
}
