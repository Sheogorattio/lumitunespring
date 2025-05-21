package com.blacksabbath.lumitunespring.mapper;

import java.util.List;
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
		System.out.println("Transformin register request into user entity");
		UserData userData = null;
		if (request.getUserData() != null) {
			System.out.println("User data is present");
			UserDataDto dto = request.getUserData();
			System.out.println("Fetched user data from request body");
			userData = new UserData();
			System.out.println("Setting up birth date");
			userData.setBirthDate(dto.getBirthDate());
			System.out.println("Setting up region");
			UUID regionId = UUID.fromString(dto.getRegionId());
			System.out.println("Region id is " + regionId);
			userData.setRegion(regionRep.findById(regionId).orElse(null));
			System.out.println("Setting up artist status");
			userData.setIsArtist(dto.getIsArtist());
			System.out.println("Setting up email");
			userData.setEmail(dto.getEmail());
		}
		System.out.println("User data does not exist");
		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(request.getPassword());
		user.setAvatar(null);
		user.setImages(List.of());
		user.setRoleId(request.getRole());
		user.setAccSubscribers(request.getAccSubscribers());
		user.setAccFollowings(request.getAccFollowings());
		user.setUserData(userData);
		System.out.println("User fields are successfuly assigned");
		if (userData != null) {
			System.out.println("User data is not present");
			userData.setUser(user);
		}
		System.out.println("Transformation to user entity ended");

		return user;
	}
}
