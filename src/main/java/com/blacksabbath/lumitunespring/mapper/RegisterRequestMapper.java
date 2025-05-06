package com.blacksabbath.lumitunespring.mapper;

import com.blacksabbath.lumitunespring.misc.RegisterRequestBody;
import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.model.UserData;

public class RegisterRequestMapper {
	 public static User toUserEntity(RegisterRequestBody request) {
	        UserData userData = null;
	        if (request.getUserData() != null) {
	            RegisterRequestBody.UserDataDto dto = request.getUserData();
	            userData = new UserData();
	            userData.setBirthDate(dto.getBirthDate());
	            userData.setRegionId(dto.getRegionId());
	            userData.setIsArtist(dto.getIsArtist());
	            userData.setEmail(dto.getEmail());
	        }

	        User user = new User();
	        user.setUsername(request.getUsername());
	        user.setPassword(request.getPassword());
	        user.setAvatarId(request.getAvatarId());
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
