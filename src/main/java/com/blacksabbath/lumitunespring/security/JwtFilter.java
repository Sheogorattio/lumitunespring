package com.blacksabbath.lumitunespring.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter implements Filter {
	
	@Autowired 
	private JwtUtil jwt;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("JWT Filter triggered for URI: " + ((HttpServletRequest) request).getRequestURI());
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

		String authHeader = httpRequest.getHeader("Authorization");
		
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			System.out.println("Token is " + token);
			String username = jwt.validateTokenAndGetUsername(token);
			
			if(username != null) {
				request.setAttribute("username", username);
				System.out.println("User was found: " + username);
				chain.doFilter(request, response);
				return;
			}	
		}
		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.setContentType("application/json");
        httpResponse.getWriter().write("{\"error\": \"Unauthorized\"}");
        httpResponse.getWriter().flush();
	}
}
