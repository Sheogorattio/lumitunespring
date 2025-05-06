package com.blacksabbath.lumitunespring;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blacksabbath.lumitunespring.misc.IsDev;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.*;

@OpenAPIDefinition(
	    info = @Info(
	        title = "LumiTune API",
	        version = "1.0",
	        description = "Документація до REST API"
	    )
	)
@SpringBootApplication
public class LumitunespringApplication {

	public static void main(String[] args) {
		
		IsDev _isDev = new IsDev();

		System.setProperty("DB_USERNAME", _isDev.getEnv("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", _isDev.getEnv("DB_PASSWORD"));
        System.setProperty("DB_ADDRESS", _isDev.getEnv("DB_ADDRESS"));
        System.setProperty("DB_NAME", _isDev.getEnv("DB_NAME"));
        System.setProperty("JWT_SECRET",_isDev.getEnv("JWT_SECRET"));

        
		SpringApplication.run(LumitunespringApplication.class, args);
	}
	

}
