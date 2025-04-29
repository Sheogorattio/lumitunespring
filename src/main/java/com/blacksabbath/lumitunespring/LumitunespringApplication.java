package com.blacksabbath.lumitunespring;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@RestController
public class LumitunespringApplication {

	public static void main(String[] args) {
		/*Dotenv dotenv = Dotenv.load();
		
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("DB_ADDRESS", dotenv.get("DB_ADDRESS"));
        System.setProperty("DB_NAME", dotenv.get("DB_NAME"));*/
		
		System.setProperty("DB_USERNAME", System.getenv("DB_USERNAME"));
	    System.setProperty("DB_PASSWORD", System.getenv("DB_PASSWORD"));
	    System.setProperty("DB_ADDRESS", System.getenv("DB_ADDRESS"));
	    System.setProperty("DB_NAME", System.getenv("DB_NAME"));
        
		SpringApplication.run(LumitunespringApplication.class, args);
	}
	
	@GetMapping("/**")
	public Map<String, String> helloWorld(){
		return Map.of("message", "Hello World!");
	}

}
