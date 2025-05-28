package com.blacksabbath.lumitunespring.misc;

public class FileOperations {

	public static String getExtension(String originalFilename) {
		if (originalFilename != null && originalFilename.contains(".")) {
			return originalFilename.substring(originalFilename.lastIndexOf("."));
		}
		return "";
	}
}
