package com.blacksabbath.lumitunespring;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.blacksabbath.lumitunespring.misc.IsDev;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.*;

@OpenAPIDefinition(
	    info = @Info(
	        title = "LumiTune API",
	        version = "0.1",
	        description = "Документація до REST API"
	    )
	)
@SpringBootApplication
public class LumitunespringApplication {

	public static void main(String[] args) {
		
		//IsDev _isDev = new IsDev();

		System.setProperty("DB_USERNAME", IsDev.getEnv("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", IsDev.getEnv("DB_PASSWORD"));
        System.setProperty("DB_ADDRESS", IsDev.getEnv("DB_ADDRESS"));
        System.setProperty("DB_NAME", IsDev.getEnv("DB_NAME"));
        System.setProperty("JWT_SECRET",IsDev.getEnv("JWT_SECRET"));

        
		SpringApplication.run(LumitunespringApplication.class, args);
	}
	

}
