package com.blacksabbath.lumitunespring.misc;

public class IsDev {
	public IsDev() {

	}
	public static String getEnv(String key) {
		return System.getenv(key);
	}
}
