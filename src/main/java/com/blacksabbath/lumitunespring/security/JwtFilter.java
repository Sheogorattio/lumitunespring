package com.blacksabbath.lumitunespring.security;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.blacksabbath.lumitunespring.misc.Aes;
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
					System.out.println("Processing jwt cookie...");
					token = cookie.getValue();
					try {
						token = Aes.decrypt(token);
					} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
							| IllegalBlockSizeException | BadPaddingException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
		
		if(request.getHeader("Authorization") != null) {
			System.out.println("Processing Authorization header...");
			token = request.getHeader("Authorization").replaceAll("Bearer ", "");
			try {
				token = Aes.decrypt(token);
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
					| BadPaddingException e) {
				e.printStackTrace();
			}
		}

		if (token != null && jwtUtil.isTokenValid(token)) {
			UUID id = UUID.fromString(jwtUtil.getSubject(token));
			User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());

			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null,
					List.of(new SimpleGrantedAuthority(jwtUtil.getRole(token)))));
		}

		filterChain.doFilter(request, response);
	}

}
