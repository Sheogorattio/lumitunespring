package com.blacksabbath.lumitunespring.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
	
    @Bean
    FilterRegistrationBean<JwtFilter> jwtFilterRegistration(JwtFilter filter){
    	FilterRegistrationBean<JwtFilter> registration= new FilterRegistrationBean<>();
    	registration.setFilter(filter);
    	registration.addUrlPatterns("/api/*");
    	return registration;
	}
}
