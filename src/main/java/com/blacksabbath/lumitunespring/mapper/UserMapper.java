package com.blacksabbath.lumitunespring.mapper;

import java.util.UUID;

import com.blacksabbath.lumitunespring.dto.UserDataDto;
import com.blacksabbath.lumitunespring.dto.UserDto;
import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.model.UserData;

public class UserMapper {
	
	public static UserDto toDto(User user) {
		if(user == null) return null;
		
		UserDto dto = new UserDto();
		dto.setId(user.getId().toString());
		dto.setUsername(user.getUsername());
		dto.setPassword(user.getPassword());
		dto.setAvatarId(user.getAvatarId());
		dto.setRole(user.getRole());
		dto.setAccSubscribers(user.getAccSubscribers());
		dto.setAccFollowings(user.getAccFollowings());
		
		if(user.getUserData() != null) {
			UserDataDto dataDto = new UserDataDto();
			dataDto.setId(user.getUserData().getId().toString());
			dataDto.setBirthDate(user.getUserData().getBirthDate());
			dataDto.setEmail(user.getUserData().getEmail());
			dataDto.setIsArtist(user.getUserData().getIsArtist());
			dataDto.setRegionId(user.getUserData().getRegionId());
		}
		
		return dto;
	}
	
	public static void updateEntity(User user, UserDto dto) {
		if(user == null || dto == null) {
			return;
		}
		
		user.setUsername(dto.getUsername());
		user.setPassword(dto.getPassword());
		user.setAvatarId(dto.getAvatarId());
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
			data.setRegionId(dataDto.getRegionId());
			data.setEmail(dataDto.getEmail());
			data.setIsArtist(dataDto.getIsArtist());
		}
	}
}
