package com.blacksabbath.lumitunespring.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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

import com.blacksabbath.lumitunespring.dto.RegionDto;
import com.blacksabbath.lumitunespring.service.RegionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/regions")
@CrossOrigin
@Tag(name = "Region related operations", description = "Операції над регіонами")
public class RegionController {

	@Autowired
	private RegionService service;

	@GetMapping("/countries")
	@Operation(summary = "Отримати всі країни", description = "Повертає список регіонів типу COUNTRY")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Успішно", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegionDto.class))),
			@ApiResponse(responseCode = "500", description = "Помилка сервера") })
	public ResponseEntity<List<RegionDto>> getAllCountries(HttpServletRequest request, HttpServletResponse response) {
		return ResponseEntity.ok(service.getAllCountries());
	}

	@GetMapping("/cities")
	@Operation(summary = "Отримати всі міста", description = "Повертає список регіонів типу CITY")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Успішно", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegionDto.class))),
			@ApiResponse(responseCode = "500", description = "Помилка сервера") })
	public ResponseEntity<List<RegionDto>> getAllCities(HttpServletRequest request, HttpServletResponse response) {
		return ResponseEntity.ok(service.getAllCities());
	}

	@GetMapping("/parent/{parentId}")
	@Operation(summary = "Отримати дочірні регіони", description = "Повертає список регіонів, які мають заданого батька")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Успішно", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegionDto.class))),
			@ApiResponse(responseCode = "204", description = "Немає вмісту"),
			@ApiResponse(responseCode = "500", description = "Помилка сервера") })
	public ResponseEntity<List<RegionDto>> getRegionsByParentId(@PathVariable String parentId,
			HttpServletRequest request, HttpServletResponse response) {
		List<RegionDto> resp = service.getRegionsByParentId(UUID.fromString(parentId));

		if (resp.isEmpty()) {
			return ResponseEntity.status(HttpServletResponse.SC_NO_CONTENT).build();
		}
		return ResponseEntity.ok(service.getRegionsByParentId(UUID.fromString(parentId)));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Отримати регіон за ID", description = "Повертає один регіон за його ідентифікатором")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Знайдено", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegionDto.class))),
			@ApiResponse(responseCode = "404", description = "Регіон не знайдено", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class), examples = @ExampleObject(value = "{\"error\": \"Region not found\"}"))),
			@ApiResponse(responseCode = "500", description = "Помилка сервера") })
	public ResponseEntity<?> getById(@PathVariable String id, HttpServletRequest request,
			HttpServletResponse response) {
		RegionDto region = service.getById(id).orElse(null);
		if (region == null) {
			return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).build();
		}
		return ResponseEntity.ok(region);
	}

	@PostMapping("/api/create")
	@Operation(summary = "Створити регіон", description = "Створює новий регіон")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Успішно створено", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegionDto.class))),
			@ApiResponse(responseCode = "500", description = "Помилка при створенні регіону") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createRegion(@RequestBody RegionDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		return ResponseEntity.ok(service.save(dto));
	}

	@PostMapping("/api/delete")
	@Operation(summary = "Видалити регіон", description = "Видаляє регіон та його дочірні елементи")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Успішно видалено"),
			@ApiResponse(responseCode = "500", description = "Помилка при видаленні регіону") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteRegion(@RequestBody RegionDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		service.delete(dto);
		return ResponseEntity.status(HttpServletResponse.SC_OK).build();
	}

	@PutMapping("/api/edit")
	@Operation(summary = "Редагувати регіон", description = "Оновлює існуючий регіон за його ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Успішно оновлено", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegionDto.class))),
			@ApiResponse(responseCode = "500", description = "Помилка при оновленні регіону", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class), examples = @ExampleObject(value = "{\"error\": \"Failed to update region\"}"))) })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> editRegion(@RequestBody RegionDto dto, HttpServletRequest request,
			HttpServletResponse response) {
		RegionDto updatedDto = service.update(dto).orElse(null);
		if (updatedDto == null) {
			return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body("Failed to update region");
		}

		return ResponseEntity.status(HttpServletResponse.SC_OK).body(service.save(updatedDto));
	}
}
