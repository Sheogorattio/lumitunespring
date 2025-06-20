package com.blacksabbath.lumitunespring.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blacksabbath.lumitunespring.dto.UserDto;
import com.blacksabbath.lumitunespring.mapper.RegisterRequestMapper;
import com.blacksabbath.lumitunespring.mapper.UserMapper;
import com.blacksabbath.lumitunespring.misc.Aes;
import com.blacksabbath.lumitunespring.misc.LoginRequestBody;
import com.blacksabbath.lumitunespring.misc.RegisterRequestBody;
import com.blacksabbath.lumitunespring.model.Artist;
import com.blacksabbath.lumitunespring.model.EmailVerification;
import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.security.JwtUtil;
import com.blacksabbath.lumitunespring.service.ArtistService;
import com.blacksabbath.lumitunespring.service.EmailService;
import com.blacksabbath.lumitunespring.service.EmailVerificationService;
import com.blacksabbath.lumitunespring.service.PlaylistService;
import com.blacksabbath.lumitunespring.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/auth")
@CrossOrigin
@Tag(name = "Authentication", description = "Операції авторизації та аутентифікації")
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private RegisterRequestMapper registerRequestMapper;
	
	@Autowired 
	private ArtistService artistService;

	@Autowired
	private JwtUtil jwt;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private EmailVerificationService emailVerificationService;
	
	@Autowired
	private PlaylistService playlistService;

	@PostMapping("/sign-up")
	@Operation(summary = "Реєстрація нового користувача", description = "Створює нового користувача з переданими даними")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Користувача створено успішно", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
			@ApiResponse(responseCode = "400", description = "Некоректні дані користувача", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))) })
	public ResponseEntity<?> createUser(@RequestBody RegisterRequestBody user, HttpServletResponse response) {
		if (!userService.isNicknameUnique(user.getUsername()))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Nickname already exists"));
		try {
			user.setPassword(Aes.decrypt(user.getPassword()));
			User createdUser = userService.createUser(user);	
			   if (user.getUserData() != null && Boolean.TRUE.equals(user.getUserData().getIsArtist())) {
		            Artist artist = new Artist();
		            artist.setUser(createdUser);
		            artistService.createArtist(artist);
		        }
			
		String token = jwt.generateToken(createdUser);
		token = Aes.encrypt(token);	
		int maxAge = Integer.parseInt(System.getenv("JWT_EXP_MS")) / 1000;
		String cookieHeader = jwt.getCookieHeader("jwt", token, maxAge);
		response.setHeader("Set-Cookie", cookieHeader);		
		
		UserDto userDto = userMapper.toDto(createdUser, true);
		EmailVerification emailVerification = emailVerificationService.createNew(createdUser);
		String messageText = String.format("""
				Hi %s,

				Please verify your email address to activate your Lumitune account. This link is valid for 1 hour. If you don’t complete the verification, your account will be permanently deleted.
				
				👉 %s
				
				Thank you for choosing Lumitune!
				
				— The Lumitune Team
				""", userDto.getUsername(), System.getenv("BACKEND_LINK")+"auth/email-verification/"+ emailVerification.getId().toString());
		
			emailService.sendSimpleMessage(user.getUserData().getEmail(), "Account verification",messageText);
			
			playlistService.createPlaylist("Улюблені треки", createdUser);		 

			userDto.setPassword(null);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("user", userDto, "token", token));
			
		} catch (Exception e) {
			System.out.println(e.fillInStackTrace());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Invalid data"));
		}
	}
 
	@PostMapping("/login")
	@Operation(summary = "Увійти", description = "Аутентифікує користувача за логіном і паролем та повертає JWT")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Успішна аутентифікація", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class), examples = @ExampleObject(name = "Successful login", summary = "Приклад відповіді при успішному логіні", value = """
											{
					  "user": {
					    "username": "string"
					  },
					  "token" : "string"
					}
											"""))),
			@ApiResponse(responseCode = "401", description = "Невірне ім’я користувача або пароль", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class), examples = @ExampleObject(name = "Failed login", summary = "Приклад відповіді при провальному логіні", value = """
					"Invalid credentials"
					"""))) })
	public ResponseEntity<?> login(@RequestBody LoginRequestBody user, HttpServletResponse response) {
		try {
			Optional<User> optionalUser = userService.findByUsername(user.getUsername());

			if (optionalUser.isEmpty() || !optionalUser.get().getPassword().equals(Aes.decrypt(user.getPassword()))) {
				return ResponseEntity.status(401).body("Invalid credenials");
			}
			
			String token = jwt.generateToken(optionalUser.get());
			token = Aes.encrypt(token);
			int maxAge = Integer.parseInt(System.getenv("JWT_EXP_MS")) / 1000;

			String cookieHeader = "jwt=" + token + "; Max-Age=" + maxAge + "; Path=/" + "; HttpOnly" + "; Secure"
					+ "; SameSite=None";

			response.setHeader("Set-Cookie", cookieHeader);

			UserDto userDto = userMapper.toDto(optionalUser.get(),true);
			return ResponseEntity.ok(Map.of("user", Map.of("username", userDto.getUsername(), "token", token)));
		} catch (Exception ex) {
			System.out.println(this.getClass().getName() + ":login: " + ex.getMessage());
			return ResponseEntity.status(500).body(ex.getMessage());
		}
	}

	@GetMapping("/isunique/{nickname}") 
	@Operation(summary = "Перевірити унікальність нікнейму", description = "Повертає true, якщо нікнейм ще не використовується")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Результат перевірки унікальності") })
	public ResponseEntity<Boolean> isNicknameUnique(@PathVariable String nickname, HttpServletResponse response) {
		return ResponseEntity.ok(userService.isNicknameUnique(nickname));
	}
	
	@GetMapping("/email-verification/{recordId}")
	@Operation(summary = "Підтвердити пошту", description = "Використовужться для верифікації пошти")
	@ApiResponses(value = { @ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404") })
	public ResponseEntity<?> verifyEmail(@PathVariable UUID recordId, HttpServletResponse response){
		try {
			emailVerificationService.verifyAccount(recordId);
			return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(System.getenv("WEB_FRONTEND_LINK"))).build();
		} catch (NotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}
}
