package com.blacksabbath.lumitunespring.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.blacksabbath.lumitunespring.misc.Roles;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private final SecretKey key = Keys.hmacShaKeyFor(System.getProperty("JWT_SECRET").getBytes());
	
	
	@SuppressWarnings("deprecation")
	public String generateAccessToken(String nickname, Roles role) {
        return Jwts.builder()
                .subject(nickname)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .signWith(key)
                .compact();
    }

    @SuppressWarnings("deprecation")
	public String generateRefreshToken(String nickname) {
        return Jwts.builder()
                .subject(nickname)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                .signWith(key)
                .compact();
    }
	
	@SuppressWarnings("deprecation")
	public Claims validateTokenAndGetClaims(String token) {
		try {
			return Jwts.parser()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody();
		}
		catch(JwtException e) {
			return null;
		}
	}
	
}
 