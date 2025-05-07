package com.blacksabbath.lumitunespring.misc;

import io.github.cdimascio.dotenv.Dotenv;

public class IsDev {
	private static Dotenv dotenv = Dotenv.load();
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
		//return dotenv.get(key);
		//return (dotenv != null && dotenv.get(key) != null) ? dotenv.get(key) : System.getenv(key);
	}
}
