package com.blacksabbath.lumitunespring.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private final SecretKey key = Keys.hmacShaKeyFor(System.getProperty("JWT_SECRET").getBytes());
	
	
	@SuppressWarnings("deprecation")
	public String genarateToken(String nickname) {
		System.out.println("genarateToken");
		return Jwts.builder()
				.subject(nickname)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 3600000))
				.signWith(key)
				.compact();
	}
	
	@SuppressWarnings("deprecation")
	public String validateTokenAndGetUsername(String token) {
		System.out.println("validateTokenAndGetUsername");
		try {
			return Jwts.parser()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
		}
		catch(JwtException e) {
			return null;
		}
	}
}
 