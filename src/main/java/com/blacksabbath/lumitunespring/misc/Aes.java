package com.blacksabbath.lumitunespring.misc;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Aes {
	
	private static String key;
	
	public Aes(	@Value("${AES_KEY}") String key) {
		Aes.key = key;
	}
	
	public static String encrypt(String plainText) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("AES");
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encrypted = cipher.doFinal(plainText.getBytes());
		return Base64.getEncoder().encodeToString(encrypted);
	}
	
	public static String decrypt(String encryptedText) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
		return new String(decrypted);
	}
}
