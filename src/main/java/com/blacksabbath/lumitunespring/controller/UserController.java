package com.blacksabbath.lumitunespring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blacksabbath.lumitunespring.dto.UserDto;
import com.blacksabbath.lumitunespring.mapper.UserMapper;
import com.blacksabbath.lumitunespring.misc.AccessChecker;
import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/users")
@CrossOrigin

@Tag(name = "User related operations", description = "Операції над користувачами")
public class UserController {

	private final UserService userService;

	private final AccessChecker accessChecker;
	
	private final UserMapper userMapper;

	public UserController(UserService userService, AccessChecker accessChecker, UserMapper userMapper) {
		this.userService = userService;
		this.accessChecker = accessChecker;
		this.userMapper = userMapper;
	}

	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Отримання всіх користувачів", description = "Доступно тільки з обліковки адміна")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Успішне отримання користувачів", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class), examples = @ExampleObject(name = "Успішно виконаний запит", value = """
					[
					    {
					        "id": "7360a2a6-1bb8-4dd4-aba1-c361c6f09fde",
					        "username": "john_doew",
					        "password": "pass1234",
					        "avatarId": "avatar1223",
					        "role": "USER",
					        "accSubscribers": 0,
					        "accFollowings": 0,
					        "userData": {
					            "id": "e6d12419-526a-4840-adef-79cdb04ba957",
					            "birthDate": "1990-01-01",
					            "regionId": "region001",
					            "isArtist": true,
					            "email": "john.doe!!!!!!!!!!!!!!!!!!!!@example.com",
					            "deleteDate": null
					        }
					    },
					    {
					        "id": "ff9fd472-82f9-4ee3-aaf8-e4055a2bf4f7",
					        "username": "admin",
					        "password": "admin",
					        "avatarId": "avatar123",
					        "role": "ADMIN",
					        "accSubscribers": 0,
					        "accFollowings": 0,
					        "userData": {
					            "id": "88f992f7-9125-445d-ae30-34c5d52cb871",
					            "birthDate": "1990-01-01",
					            "regionId": "region001",
					            "isArtist": true,
					            "email": "admin@example.com",
					            "deleteDate": null
					        }
					    }
					]
					"""))),
			@ApiResponse(responseCode = "403", description = "Спроба виконати запит не як адміністратор", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class), examples = @ExampleObject(name = "Немає прав доступу", value = """
					{
					  "error": "Unauthorized"
					}
					"""))) })
	public ResponseEntity<List<UserDto>> getAllUsers(HttpServletRequest request, HttpServletResponse response) {

		if (!accessChecker.Check(request, null)) {
			return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).build();
		}

		List<UserDto> userDtos = userService.getAllUsers().stream().map(e -> userMapper.toDto(e, true)).toList();
		System.out.println("Returning users: " + userDtos);

		if (userDtos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(userDtos);

	}

	@GetMapping("/id/{id}")
	@Operation(summary = "Отримати користувача за ID", description = "Доступно власнику облікового запису або адміну")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Користувача знайдено", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
			@ApiResponse(responseCode = "403", description = "Немає прав доступу", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class), examples = @ExampleObject(name = "Немає прав доступу", value = """
					{
					  "error": "Unauthorized"
					}
					"""))),
			@ApiResponse(responseCode = "404", description = "Користувача не знайдено", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class), examples = @ExampleObject(name = "Об'єект не знайдено", value = """
					{
					  "Error: response status is 404"
					}
					"""))) })
	public ResponseEntity<UserDto> getById(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
		Optional<User> user = userService.getById(id);

		if (!user.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		User existingUser = user.get();
		UserDto userDto = userMapper.toDto(existingUser, true);
		if (!accessChecker.Check(request, UUID.fromString(id))) {
			return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).build();
		}

		return ResponseEntity.ok(userDto);
	}

	@GetMapping("/username/{username}")
	@Operation(summary = "Отримати користувача за username", description = "Доступно власнику облікового запису або адміну")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Користувача знайдено", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
			@ApiResponse(responseCode = "403", description = "Немає прав доступу", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class), examples = @ExampleObject(name = "Немає прав доступу", value = """
					{
					  "error": "Unauthorized"
					}
					"""))),
			@ApiResponse(responseCode = "404", description = "Користувача не знайдено", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class), examples = @ExampleObject(name = "Об'єект не знайдено", value = """
					{
					  "Error: response status is 404"
					}
					"""))) })
	public ResponseEntity<?> getByUsername(@PathVariable String username, HttpServletRequest request,
			HttpServletResponse response) {
		Optional<User> user = userService.findByUsername(username);

		if (!user.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		User existingUser = user.get();
		if (!accessChecker.Check(request, existingUser.getId())) {
			return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).build();
		}

		return ResponseEntity.ok(userMapper.toDto(existingUser,false));
	}

	@PutMapping("/edit")
	@Operation(summary = "Редагувати дані користувача", description = "Доступно власнику облікового запису або адміну")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Користувача успішно змінено", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
			@ApiResponse(responseCode = "403", description = "Немає прав доступу", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class), examples = @ExampleObject(name = "Немає прав доступу", value = """
					{
					  "error": "Unauthorized"
					}
					"""))),
			@ApiResponse(responseCode = "404", description = "Користувача не знайдено", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class), examples = @ExampleObject(name = "Об'єект не знайдено", value = """
					{
					  "Error: response status is 404"
					}
					"""))),
			@ApiResponse(responseCode = "500", description = "Помилка при зміні користувача") })
	public ResponseEntity<?> editById(@RequestBody UserDto user, HttpServletRequest request,HttpServletResponse response) {

		System.out.println(user.toString());

		Optional<User> userOpt = userService.getById(user.getId().toString());

		if (!userOpt.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		User _user = userOpt.get();

		if (!accessChecker.Check(request, UUID.fromString(user.getId()))) {
			return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).build();
		}

		userMapper.updateEntity(_user, user);

		Optional<User> editedUserOpt = userService.editUserById(userMapper.toDto(_user,true));
		if (!editedUserOpt.isPresent()) {
			return ResponseEntity.status(500).body("Somesing went wrong. Try later.");
		}
		User editedUser = editedUserOpt.get();

		return ResponseEntity.ok(userMapper.toDto(editedUser,true));
	}
  
	@PostMapping("/logout")
	@Operation(summary = "Вийти з акаунту (очистити куки)", description = "Видаляє refresh та access токени з браузера")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Успішний вихід"),
			@ApiResponse(responseCode = "403", description = "Немає прав доступу", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class), examples = @ExampleObject(name = "Немає прав доступу", value = """
					{
					  "error": "Unauthorized"
					}
					"""))) })
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		try {
			Arrays.stream(request.getCookies())
			.filter(cookie -> cookie.getName().equals("jwt"))
			.forEach(cookie -> {
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
			});
			SecurityContextHolder.getContext().setAuthentication(null);
			return ResponseEntity.ok().build();
		}
		catch(Exception e) {
			System.out.println("'UserController.logout':"+e.getMessage());
			return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/current") 
	@Operation(summary = "Отримати поточного користувача")
	public ResponseEntity<?> current(HttpServletRequest request, HttpServletResponse response){
		try {
			return ResponseEntity.ok(userService.getCurrentUser());
		}
		catch(AccessDeniedException ex) { 
			return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).body("Invalid user data.");
		}
		catch(AuthenticationCredentialsNotFoundException ex) {
			return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Unauthorized. Principal is not present.");
		}
		catch(RuntimeException ex) {
			return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).build();
		}
	}
	
	 @PostMapping("/{subscriberId}/subscribe/{targetUserId}")
	 @Operation(summary = "Підписатися на користувача")
	    public ResponseEntity<?> subscribe(@PathVariable UUID subscriberId, @PathVariable UUID targetUserId) {
	        UserDto updatedUser;
			try {
				updatedUser = userService.subscribeTo(subscriberId, targetUserId);
			} catch (NotFoundException e) {
				e.printStackTrace();
				return ResponseEntity.notFound().build();
			}
			catch (Exception e) { 
				return ResponseEntity.status(500).body(e.getMessage());
			}
	        return ResponseEntity.ok(updatedUser);
	    }
}
