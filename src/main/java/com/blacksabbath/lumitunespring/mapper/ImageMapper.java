package com.blacksabbath.lumitunespring.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.blacksabbath.lumitunespring.dto.ImageDto;
import com.blacksabbath.lumitunespring.model.Image;
import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.repository.UserRepository;

public class ImageMapper {

	@Autowired
	private static UserRepository userRepo;

	public static ImageDto toDto(Image entity) {
		if (entity == null)
			return null;
		ImageDto dto = new ImageDto();
		dto.setId(entity.getId().toString());
		dto.setUrl(entity.getUrl());
		dto.setOwner(entity.getOwner().getId().toString());
		return dto;
	}

	public static List<ImageDto> toDto(List<Image> images) {
		List<ImageDto> dtos = new ArrayList<ImageDto>();
		for (Image e : images) {
			dtos.add(toDto(e));
		}
		return dtos;
	}

	public static Image toEntity(ImageDto dto) throws Exception {
		if (dto == null)
			return null;
		if (dto.getId() == null || dto.getUrl() == null) {
			throw new Exception(ImageMapper.class.getName() + "::toEntity"
					+ " : invalid ImageDto instance. Dto.id or Dto.url is null.");
		}
		User owner = userRepo.findById(UUID.fromString(dto.getOwner())).orElse(null);
		if (owner == null) {
			throw new Exception(ImageMapper.class.getName() + "::toEntity" + " : owner does not exist.");
		}
		return new Image(UUID.fromString(dto.getId()), dto.getUrl(), owner);
	}

	public static List<Image> toEntity(List<ImageDto> dtos) throws Exception {
		List<Image> images = new ArrayList<Image>();
		for (ImageDto d : dtos) {
			images.add(toEntity(d));
		}
		return images;
	}
}
