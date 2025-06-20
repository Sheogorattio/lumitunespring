package com.blacksabbath.lumitunespring.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.blacksabbath.lumitunespring.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Value("${JWT_SECRET}")
	private String jwtSecret;

	@Value("${JWT_EXP_MS}")
	private int expirationMs;

	public String generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", "ROLE_" + user.getRole().name().toUpperCase());
		return Jwts.builder()
				.claims(claims)
				.subject(user.getId().toString())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + expirationMs))
				.signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())).compact();
	}
	
	public String getCookieHeader(String name, String value, int maxAge) {
		return name + "=" + value + "; Max-Age=" + maxAge + "; Path=/" + "; HttpOnly" + "; Secure"
				+ "; SameSite=None";
	} 

	public Claims extractAllClaims(String token) {
		SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	} 

	public Date getExpirationDate(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public String getSubject(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public String getRole(String token) {
		return extractClaim(token, claims -> claims.get("role", String.class));
	}

	public Boolean isTokenExpired(String token) {
		return getExpirationDate(token).before(new Date());
	}

	public Boolean isTokenValid(String token) {
		try {
			extractAllClaims(token);
			return !isTokenExpired(token);
		} catch (JwtException | IllegalArgumentException ex) {
			return false;
		}
	}

}
