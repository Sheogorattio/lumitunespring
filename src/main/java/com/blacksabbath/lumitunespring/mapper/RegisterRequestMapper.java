package com.blacksabbath.lumitunespring.mapper;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blacksabbath.lumitunespring.dto.UserDataDto;
import com.blacksabbath.lumitunespring.misc.RegisterRequestBody;
import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.model.UserData;
import com.blacksabbath.lumitunespring.repository.ImageRepository;
import com.blacksabbath.lumitunespring.repository.RegionRepository;

@Component
public class RegisterRequestMapper {

	@Autowired
	private RegionRepository regionRep;

	@Autowired
	private ImageRepository imageRep;

	public User toUserEntity(RegisterRequestBody request) {
		UserData userData = null;
		if (request.getUserData() != null) {
			UserDataDto dto = request.getUserData();
			userData = new UserData();
			userData.setBirthDate(dto.getBirthDate());
			userData.setRegion(regionRep.findById(UUID.fromString(dto.getRegionId())).orElse(null));
			userData.setIsArtist(dto.getIsArtist());
			userData.setEmail(dto.getEmail());
		}

		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(request.getPassword());
		// UUID avatarId;
		try {
			// avatarId = UUID.fromString(request.getAvatarId());
			user.setAvatar(null);
		} catch (IllegalArgumentException e) {
			// avatarId = null;
			user.setAvatar(null);
		}
		user.setRoleId(request.getRole());
		user.setAccSubscribers(request.getAccSubscribers());
		user.setAccFollowings(request.getAccFollowings());
		user.setUserData(userData);

		if (userData != null) {
			userData.setUser(user);
		}

		return user;
	}
}
