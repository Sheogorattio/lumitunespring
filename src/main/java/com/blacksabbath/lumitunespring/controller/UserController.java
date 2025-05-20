package com.blacksabbath.lumitunespring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/api/users")
@CrossOrigin

@Tag(name = "User related operations", description = "Операції над користувачами")
public class UserController {

	private final UserService userService;

	private final AccessChecker accessChecker;

	public UserController(UserService userService, AccessChecker accessChecker) {
		this.userService = userService;
		this.accessChecker = accessChecker;
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

		if (!accessChecker.Check(request, "")) {
			return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).build();
		}

		List<UserDto> userDtos = userService.getAllUsers().stream().map(UserMapper::toDto).toList();
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
	public ResponseEntity<User> getById(@PathVariable String id, HttpServletRequest request,
			HttpServletResponse response) {
		Optional<User> user = userService.getById(id);

		if (!user.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		User existingUser = user.get();
		UserDto userDto = UserMapper.toDto(existingUser);
		if (!accessChecker.Check(request, userDto.getUsername())) {
			return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).build();
		}

		return ResponseEntity.ok(existingUser);
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
		if (!accessChecker.Check(request, username)) {
			return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).build();
		}

		return ResponseEntity.ok(UserMapper.toDto(existingUser));
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

		if (!accessChecker.Check(request, user.getUsername())) {
			return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).build();
		}

		UserMapper.updateEntity(_user, user);

		Optional<User> editedUserOpt = userService.editUserById(UserMapper.toDto(_user));
		if (!editedUserOpt.isPresent()) {
			return ResponseEntity.status(500).body("Somesing went wrong. Try later.");
		}
		User editedUser = editedUserOpt.get();

		return ResponseEntity.ok(UserMapper.toDto(editedUser));
	}
  
	@PostMapping("/logout")
	@Operation(summary = "Вийти з акаунту (очистити куки)", description = "Видаляє refresh та access токени з браузера")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Успішний вихід"),
			@ApiResponse(responseCode = "403", description = "Немає прав доступу", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class), examples = @ExampleObject(name = "Немає прав доступу", value = """
					{
					  "error": "Unauthorized"
					}
					"""))) })
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		Arrays.stream(request.getCookies())
				.filter(cookie -> cookie.getName().equals("refreshToken") || cookie.getName().equals("accessToken"))
				.forEach(cookie -> {
					cookie.setMaxAge(0);
					cookie.setPath("/");
					response.addCookie(cookie);
				});
	}
}
