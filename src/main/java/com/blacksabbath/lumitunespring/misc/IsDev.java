package com.blacksabbath.lumitunespring.misc;

import io.github.cdimascio.dotenv.Dotenv;

public class IsDev {
	Dotenv dotenv = null;
	public IsDev() {
		try {
			dotenv = Dotenv.load();
			if(dotenv != null)
				System.out.println("Dotenv is not null");
			else
				System.out.println("Dotenv is null");
		}
		catch( Exception e){
			System.out.println("Dotenv is null");
		}
	}
	public static String getEnv(String key) {
		return System.getenv(key);
		//return (dotenv != null && dotenv.get(key) != null) ? dotenv.get(key) : System.getenv(key);
	}
}
