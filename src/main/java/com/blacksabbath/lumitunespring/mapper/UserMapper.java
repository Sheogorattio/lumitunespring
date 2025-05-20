package com.blacksabbath.lumitunespring.mapper;

import java.awt.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.blacksabbath.lumitunespring.dto.UserDataDto;
import com.blacksabbath.lumitunespring.dto.UserDto;
import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.model.UserData;
import com.blacksabbath.lumitunespring.repository.ImageRepository;
import com.blacksabbath.lumitunespring.repository.RegionRepository;

public class UserMapper {
	
	@Autowired
	static RegionRepository regionRep;
	
	@Autowired
	static ImageRepository imageRep;
	
	public static UserDto toDto(User user) {
		if(user == null) return null;
		
		UserDto dto = new UserDto();
		dto.setId(user.getId().toString());
		dto.setUsername(user.getUsername());
		dto.setPassword(user.getPassword());
		
		if(user.getAvatar() == null) {
			dto.setAvatar(null);
		}
		else {
			dto.setAvatar(ImageMapper.toDto(user.getAvatar()));
		}
		
		dto.setRole(user.getRole());
		dto.setAccSubscribers(user.getAccSubscribers());
		dto.setAccFollowings(user.getAccFollowings());
		
		UserDataDto dataDto = new UserDataDto();
		if(user.getUserData() != null) {
			dataDto.setId(user.getUserData().getId().toString());
			dataDto.setBirthDate(user.getUserData().getBirthDate());
			dataDto.setEmail(user.getUserData().getEmail());
			dataDto.setIsArtist(user.getUserData().getIsArtist());
			dataDto.setRegionId(user.getUserData().getRegion().getId().toString());
		}
		dto.setUserData(dataDto);
		
		return dto;
	}
	
	public static void updateEntity(User user, UserDto dto) {
		if(user == null || dto == null) {
			return;
		}
		
		user.setUsername(dto.getUsername());
		user.setPassword(dto.getPassword());
		
		try {
			if(dto.getAvatar() == null || dto.getAvatar().size() == 0) {
				user.setAvatar(null);
			}
			user.setAvatar(ImageMapper.toEntity(dto.getAvatar()));
		}
		catch(Exception e) {
			user.setAvatar(null);
			e.printStackTrace();
		}
		
		
		
		user.setRoleId(dto.getRole());
		user.setAccSubscribers(dto.getAccSubscribers());
		user.setAccFollowings(dto.getAccFollowings());
		
		if( dto.getUserData() != null) {
			if(user.getUserData() == null) {
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
	}
}
