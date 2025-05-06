package com.blacksabbath.lumitunespring.misc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.github.cdimascio.dotenv.Dotenv;

public class IsDev {
	Dotenv dotenv = null;
	public IsDev() {
		try {
			dotenv = Dotenv.load();
		}
		catch( Exception e){
			
		}
	}
	public String getEnv(String key) {
		return (dotenv != null && dotenv.get(key) != null) ? dotenv.get(key) : dotenv.get(key);//System.getenv(key);
	}
}
