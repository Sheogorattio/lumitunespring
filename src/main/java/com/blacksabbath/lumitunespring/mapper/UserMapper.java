package com.blacksabbath.lumitunespring.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blacksabbath.lumitunespring.dto.ImageDto;
import com.blacksabbath.lumitunespring.dto.UserDataDto;
import com.blacksabbath.lumitunespring.dto.UserDto;
import com.blacksabbath.lumitunespring.model.Image;
import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.model.UserData;
import com.blacksabbath.lumitunespring.repository.ImageRepository;
import com.blacksabbath.lumitunespring.repository.RegionRepository;

@Component
public class UserMapper {

	private final RegionRepository regionRep;

	private final ImageRepository imageRep;
	
	private final ImageMapper imageMapper;
	
	public UserMapper(RegionRepository regionRep, ImageRepository imageRep, ImageMapper imageMapper) {
		this.imageRep = imageRep;
		this.regionRep = regionRep;
		this.imageMapper = imageMapper;
	}

	public UserDto toDto(User user) {
		if (user == null)
			return null;

		UserDto dto = new UserDto();
		dto.setId(user.getId().toString());
		dto.setUsername(user.getUsername());
		dto.setPassword(user.getPassword());
		dto.setAvatar(imageMapper.toDto(user.getAvatar()));
		dto.setImages(imageMapper.toDto(user.getImages()));
		dto.setRole(user.getRole());
		dto.setAccSubscribers(user.getAccSubscribers());
		dto.setAccFollowings(user.getAccFollowings());

		UserDataDto dataDto = new UserDataDto();
		if (user.getUserData() != null) {
			dataDto.setId(user.getUserData().getId().toString());
			dataDto.setBirthDate(user.getUserData().getBirthDate());
			dataDto.setEmail(user.getUserData().getEmail());
			dataDto.setIsArtist(user.getUserData().getIsArtist());
			dataDto.setRegionId(user.getUserData().getRegion().getId().toString());
		}
		dto.setUserData(dataDto);

		return dto;
	}

	public void updateEntity(User user, UserDto dto) {
		if (user == null || dto == null) {
			return;
		}

		user.setUsername(dto.getUsername());
		user.setPassword(dto.getPassword());

		try {
			user.setAvatar(imageMapper.toEntity(dto.getAvatar()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			List<UUID> exIds = user.getImages().stream().map(Image::getId).collect(Collectors.toList());
			List<Image> images = imageMapper.toEntity(dto.getImages());
			List<Image> newImages = List.of();
			for(Image i: images) {
				if(!exIds.contains(i.getId())) {
					newImages.add(i);
					System.out.println("NEW Image");
				}
			}
			if(newImages.size()>0) {
				user.getImages().addAll(newImages);
			}
			
		} catch (Exception e) {
			user.setAvatar(null);
			e.printStackTrace();
		}

		user.setRoleId(dto.getRole());
		user.setAccSubscribers(dto.getAccSubscribers());
		user.setAccFollowings(dto.getAccFollowings());

		if (dto.getUserData() != null) {
			if (user.getUserData() == null) {
				user.setUserData(new UserData());
			}

			UserData data = user.getUserData();
			UserDataDto dataDto = dto.getUserData();

			data.setId(UUID.fromString(dataDto.getId()));
			data.setBirthDate(dataDto.getBirthDate());
			data.setRegion(regionRep.findById(UUID.fromString(dataDto.getRegionId())).orElse(null));
			data.setEmail(dataDto.getEmail());
			data.setIsArtist(dataDto.getIsArtist());
		}
		System.out.println("Update ended");
	}
}
