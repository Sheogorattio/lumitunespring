package com.blacksabbath.lumitunespring.misc;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blacksabbath.lumitunespring.repository.UserRepository;
import com.blacksabbath.lumitunespring.security.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class AccessChecker {

	private final UserRepository userRepo;

	private final JwtUtil jwtUtil;

	public AccessChecker(UserRepository userRepo, JwtUtil jwtUtil) {
		this.userRepo = userRepo;
		this.jwtUtil = jwtUtil;
	}

	public boolean Check(HttpServletRequest request, String nickname) {
		String token = null;

		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("jwt".equals(cookie.getName())) {
					token = cookie.getValue();
					break;
				}
			}
		}
		String roleAttr = jwtUtil.getRole(token);
		String nameAttr = jwtUtil.getSubject(token);

		if (roleAttr == null || nameAttr == null) {
			return false;
		}

		String role = roleAttr.toString();
		String userNickname = nameAttr.toString();

		System.out.println(AccessChecker.class.getName() + ":Check: role attribute is: " + role);

		if (role.equals("ROLE_" + Roles.ADMIN.toString())) {
			return true;
		}

		return userNickname.equals(nickname);
	}

	public boolean Check(HttpServletRequest request, UUID userId) {
		String userNickname = userRepo.findById(userId).map(u -> u.getUsername()).orElse(null);
		if (userNickname == null)
			return false;
		return Check(request, userNickname);
	}
}
