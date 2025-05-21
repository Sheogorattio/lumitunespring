package com.blacksabbath.lumitunespring.security;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.blacksabbath.lumitunespring.model.User;
import com.blacksabbath.lumitunespring.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.persistence.EntityNotFoundException;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	private final UserRepository userRepository;

	public JwtFilter(JwtUtil jwtUtil, UserRepository userRepository) {
		this.jwtUtil = jwtUtil;
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = null;

		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("jwt".equals(cookie.getName())) {
					token = cookie.getValue();
					break;
				}
			}
		}

		if (token != null && jwtUtil.isTokenValid(token)) {
			String username = jwtUtil.getSubject(token);
			User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException());

			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null,
					List.of(new SimpleGrantedAuthority(jwtUtil.getRole(token)))));
		}

		filterChain.doFilter(request, response);
	}

}
