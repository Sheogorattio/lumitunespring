package com.blacksabbath.lumitunespring.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity()
public class SecurityConfig {

	private final JwtFilter jwtFilter;

	public SecurityConfig(JwtFilter jwtFilter) {
		this.jwtFilter = jwtFilter;
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(List.of("https://lumitune.vercel.app", "http://127.0.0.1:*", "http://localhost:*", "https://sheogorattio.github.io/", "exp://192.168.0.*:*", "http://192.168.0.*:*"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.configurationSource(corsConfigurationSource())).csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(authorizeRequests -> authorizeRequests
						.requestMatchers("/artists/**", "/albums/**", "/content/main", "/auth/**",  "/users/username/", "/swagger", "/swagger-ui.html", "/swagger-ui/**", "/users/username/", "/regions/countries", "/regions/cities", "/regions/parent/*", "/regions", "/v3/**", "/v2/**").permitAll()
						.requestMatchers("/error", "/auth/**").anonymous()
						.anyRequest().authenticated())
				.addFilterBefore(jwtFilter,
						org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}