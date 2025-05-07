package com.blacksabbath.lumitunespring.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blacksabbath.lumitunespring.dto.UserDto;
import com.blacksabbath.lumitunespring.mapper.UserMapper;
import com.blacksabbath.lumitunespring.misc.Roles;
import com.blacksabbath.lumitunespring.service.UserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtFilter implements Filter {

    @Autowired
    private JwtUtil jwt;

    @Autowired
    private UserService userService;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
    	try {
	        HttpServletRequest httpRequest = (HttpServletRequest) request;
	        HttpServletResponse httpResponse = (HttpServletResponse) response;
	
	        String token = extractAccessToken(httpRequest);
	        System.out.println("Access token is " + token);
	        if(token != null) {
	        	Claims claims = jwt.validateTokenAndGetClaims(token);
	        	
		        if (claims != null) {
		            setAttributesAndContinue(request, response, chain, claims);
		            return;
		        }
	        }
	        
	
	        String refreshToken = extractCookie(httpRequest, "refreshToken");
	        System.out.println("Refresh token is " + refreshToken);
	        if(refreshToken == null) {
	        	sendUnnauthorizedResponse(httpResponse);
	        }
	        Claims refreshClaims = jwt.validateTokenAndGetClaims(refreshToken);
	        System.out.println("Refresh claims are: " + refreshClaims.toString());
	
	        if (refreshClaims != null) {
	            String username = refreshClaims.getSubject();
	            Roles role = userService.findByUsername(username)
		            	    .map(UserMapper::toDto)
		            	    .map(UserDto::getRole)
		            	    .orElse(Roles.GUEST);
	            System.out.println("New role is: " + role);
	
	            String newAccessToken = jwt.generateAccessToken(username, role);
	            System.out.println("New access token is " + newAccessToken);
	            if(newAccessToken == null) {
		        	sendUnnauthorizedResponse(httpResponse);
		        }
	            setAccessTokenCookie(httpResponse, newAccessToken);
	
	            setAttributesAndContinue(request, response, chain, jwt.validateTokenAndGetClaims(newAccessToken));
	            return;
	        }
    	}
    	catch(Exception ex) {
    		System.out.println(ex.getMessage());
    		return;
    	}
    }
    
    private void sendUnnauthorizedResponse(HttpServletResponse response) throws IOException {
    	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    	response.setContentType("application/json");
    	response.getWriter().write("{\"error\":\"Unauthorized\"}");
    	response.getWriter().flush();
    }

    private void setAttributesAndContinue(ServletRequest request, ServletResponse response, FilterChain chain, Claims claims)
            throws IOException, ServletException {
        request.setAttribute("username", claims.getSubject());
        request.setAttribute("role", Roles.valueOf((String) claims.get("role")));
        chain.doFilter(request, response);
    }

    private String extractAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return extractCookie(request, "accessToken");
    }

    private String extractCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private void setAccessTokenCookie(HttpServletResponse response, String token) {
        String cookie = "accessToken=" + token +
                "; HttpOnly; Secure; SameSite=Strict; Path=/; Max-Age=3600";
        response.addHeader("Set-Cookie", cookie);
    }

}
